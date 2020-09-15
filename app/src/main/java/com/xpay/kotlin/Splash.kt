package com.xpay.kotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long=3000 // 3 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("save", Context.MODE_PRIVATE)
//        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        val  isactive:Boolean= sharedPreferences.getBoolean("active",false)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            if(isactive){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivity(Intent(this,Login::class.java))
            }


            // close this activity
            finish()
        }, SPLASH_TIME_OUT)

//        editor.putBoolean("active",false).apply()



    }
}