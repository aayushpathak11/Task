package com.example.task

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.forgot_layout.view.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        btnRegLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        btn_login.setOnClickListener {
            when {
                TextUtils.isEmpty(email_log.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please Enter Valid email id", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(pass_log.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email: String = email_log.text.toString().trim { it <= ' ' }
                    val password: String = pass_log.text.toString().trim { it <= ' ' }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    Toast.makeText(
                                        this,
                                        "You were logged in successfully ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, ListActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.putExtra(
                                        "USER ID",
                                        FirebaseAuth.getInstance().currentUser!!.uid
                                    )
                                    intent.putExtra(
                                        "email id",
                                        FirebaseAuth.getInstance().currentUser!!.email
                                    )
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                }
            }
        }
        forgot.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val v = inflater.inflate(R.layout.forgot_layout, null)
            val addDialog = AlertDialog.Builder(this)
            addDialog.setView(v)
            addDialog.setPositiveButton("SUBMIT") { dialog, _ ->
                when {
                    TextUtils.isEmpty(v.email_forgot.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show()
                    }
                    else-> {
                        val names: String = v.email_forgot.text.toString().trim { it <= ' ' }
                        FirebaseAuth.getInstance().sendPasswordResetEmail(names)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Email sent to your mail ID to RESET Password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            }
            addDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
            addDialog.setTitle("PASSWORD RESET !!")
            addDialog.setIcon(R.drawable.ic_lock)
            addDialog.setCancelable(false)
            addDialog.create()
            addDialog.show()
        }
    }
    override fun onBackPressed() {
        val builder: AlertDialog.Builder =  AlertDialog.Builder(this)
        builder.setTitle("EXIT..!! ")
        builder.setIcon(R.drawable.exit_icon)
        builder.setMessage("Are you sure you want to EXIT the App")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialoginterface: DialogInterface, i: Int ->
            super.onBackPressed()
            finish()
        }
        builder.setNegativeButton("NO"){ dialoginterface: DialogInterface, i:Int->
        }
        val builder1=builder.create()
        builder1.show()
    }
}