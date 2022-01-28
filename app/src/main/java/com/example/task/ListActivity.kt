package com.example.task

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.add_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ListActivity : AppCompatActivity() {
    private var databasereference: DatabaseReference? = null
    var database:FirebaseDatabase?=null

    private lateinit var userlist: ArrayList<UserData>
    private lateinit var useradapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        databasereference = database?.reference?.child("item")

        setContentView(R.layout.activity_list)
        supportActionBar!!.hide()
        btn_logout.setOnClickListener {
            val builder:AlertDialog.Builder =  AlertDialog.Builder(this)
            builder.setTitle("LOGOUT..!! ")
            builder.setIcon(R.drawable.exit_icon)
            builder.setMessage("Are you sure you want to LOGOUT ? ")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialoginterface: DialogInterface, i: Int ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
            builder.setNegativeButton("NO"){dialoginterface:DialogInterface,i:Int->
            }
            val builder1=builder.create()
            builder1.show()

        }
        userlist = ArrayList()
        useradapter = UserAdapter(this, userlist)
        rv.layoutManager=LinearLayoutManager(this)
        rv.adapter = useradapter
        addingBtn.setOnClickListener { addinfo() }
        val auth = FirebaseAuth.getInstance()
        loadlist()
    }

    private fun loadlist() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val us = auth.currentUser?.uid
        db.collection("$us").addSnapshotListener(object:EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error!=null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for(dc:DocumentChange in value?.documentChanges!!){
                    if(dc.type==DocumentChange.Type.ADDED){
                        userlist.add(dc.document.toObject(UserData::class.java))
                    }
                }
                useradapter.notifyDataSetChanged()
            }
        })
    }

    private fun addinfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item,null)
        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)

        addDialog.setPositiveButton("SUBMIT"){
            dialog,_->
            when{
                TextUtils.isEmpty(v.et_name.text.toString().trim{it<= ' '})->{
                    Toast.makeText(this,"Please Enter Name",Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.et_price.text.toString().trim{it<= ' '})->{
                    Toast.makeText(this,"Please Enter Price",Toast.LENGTH_SHORT).show()
                }
                else->{
                    val names:String = v.et_name.text.toString().trim{it<= ' '}
                    val prices = v.et_price.text.toString().trim{it<= ' '}
                    val sdf:SimpleDateFormat = SimpleDateFormat("hh:mm a yyyy-MM-dd")
                    val final:String = sdf.format(Date())
                    val db = FirebaseFirestore.getInstance()
                    val user:MutableMap<String,Any> = HashMap()
                    user["itemName"] = names
                    user["itemPrice"] = prices
                    user["itemTime"] = final
                    val auth = FirebaseAuth.getInstance()
                    val cs = auth.currentUser?.uid
                    db.collection("$cs")
                        .add(user)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Record added Successfully",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            Toast.makeText(this, "Record failed to add",Toast.LENGTH_SHORT).show()
                        }
                    dialog.dismiss()
                }
            }
        }
        addDialog.setNegativeButton("Cancel"){
            dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
        }
        addDialog.setCancelable(false)
        addDialog.create()
        addDialog.show()

    }
    override fun onBackPressed() {
        val builder:AlertDialog.Builder =  AlertDialog.Builder(this)
        builder.setTitle("EXIT..!! ")
        builder.setIcon(R.drawable.exit_icon)
        builder.setMessage("Are you sure you want to EXIT the App")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialoginterface: DialogInterface, i: Int ->
            finish()

        }
        builder.setNegativeButton("NO"){dialoginterface:DialogInterface,i:Int->
        }
        val builder1=builder.create()
        builder1.show()
    }
}