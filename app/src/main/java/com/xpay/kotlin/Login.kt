package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlin.models.PayResponse
import com.xpay.kotlin.models.PrepareAmount
import com.xpay.kotlin.models.User
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_movie.*


class Login : AppCompatActivity() {
    var dialog: AlertDialog? =null
    var sharedPreferences: SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        XpayUtils.apiKey = "3uBD5mrj.3HSCm46V7xJ5yfIkPb2gBOIUFH4Ks0Ss"
        XpayUtils.communityId = "zogDmQW"
        XpayUtils.currency = "EGP"
        XpayUtils.payUsing = "card"
        XpayUtils.variableAmountID = 18
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
         sharedPreferences =
            this.getSharedPreferences("save", Context.MODE_PRIVATE)

//        editor.putBoolean("active",true).apply()
//        val dialog = setProgressDialog(this, "Loading..")
//        dialog.show()
        dialog= SpotsDialog.Builder().setContext(this@Login).build()

        btnLogin.setOnClickListener {

           val num: Number = userAmount.text.toString().toDouble()
            dialog?.show()
            XpayUtils.prepareAmount(num, XpayUtils.communityId!!,::userSuccess,::userFailure)
//            Handler().postDelayed({
//                dialog.dismiss()
//
//            }, 2000);

        }
    }

    fun userSuccess(res: PrepareAmount) {
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        dialog?.dismiss()
        Toast.makeText(this, res.data.total_amount.toString(), Toast.LENGTH_LONG).show()
        XpayUtils.amount=userAmount.text.toString().toDouble()
        startActivity(Intent(this, UserActivity::class.java))
//        editor.putFloat("amount",userAmount.text.toString().toFloat()).apply()

    }

    fun userFailure(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }

}