package com.xpay.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.xpay.kotlin.models.User
import com.xpay.kotlinutils.XpayUtils
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        btnSubmit.setOnClickListener {
            val fullName: String = userName2.text.toString()
            val email: String = userEmail.text.toString()
            val phone = "${userPhone.text}"

            if(fullName.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
                XpayUtils.user = User(fullName, email, phone)
            }else{
                if(fullName.isEmpty())
                    userName2.setError("Enter valid Full Name")
                if(email.isEmpty())
                    userEmail.setError("Enter valid Email")
                if(phone.isEmpty())
                    userPhone.setError("Enter valid Phone Number")
            }


//                Toast.makeText(this,"EError",Toast.LENGTH_LONG)
        }
    }
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}