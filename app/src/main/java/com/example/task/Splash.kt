package com.example.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import androidx.core.os.HandlerCompat.postDelayed

import androidx.core.view.*
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        val lottie= findViewById<LottieAnimationView>(R.id.lottie)
        lottie.animate().setDuration(2700).setStartDelay(2900)
        val handler=Handler()
        val auth = FirebaseAuth.getInstance()
        val curr = auth.currentUser
        if(curr!=null){
            handler.postDelayed({
                val intent = Intent(this,ListActivity::class.java)
                startActivity(intent)
                finish()
            },2500)
        }
        else{
            handler.postDelayed({
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            },2500)
        }
    }
}



