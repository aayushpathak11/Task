package com.example.task

import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar!!.hide()
        button_reg.setOnClickListener {
            when{
                TextUtils.isEmpty(email_reg.text.toString().trim{it<= ' '})->{
                    Toast.makeText(this,"Please Enter Valid email id",Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(pass_reg.text.toString().trim{it<= ' '})->{
                    Toast.makeText(this,"Please Enter Valid Password",Toast.LENGTH_SHORT).show()
                }
                else->{
                    val email:String =email_reg.text.toString().trim{it<= ' '}
                    val password:String =pass_reg.text.toString().trim{it<= ' '}
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful){
                                val firebaseUser:FirebaseUser = task.result!!.user!!
                                Toast.makeText(this,"You were successfully Registered",Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,ListActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("USER ID",firebaseUser.uid)
                                intent.putExtra("email id",email)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
        btnLogRegister.setOnClickListener{
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }
}


