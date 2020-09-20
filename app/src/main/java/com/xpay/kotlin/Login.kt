package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlin.models.PrepareAmount
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var sharedPreferences: SharedPreferences? = null

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

        dialog = SpotsDialog.Builder().setContext(this@Login).build()

        btnLogin.setOnClickListener {

            var num: Number? =null

            if (userAmount.text.toString().isNotEmpty()) {
                num=userAmount.text.toString().toDouble()
                dialog?.show()
                XpayUtils.prepareAmount(XpayUtils.apiKey!!,num, XpayUtils.communityId!!, ::userSuccess, ::userFailure)
            } else {
                userAmount.setError("Enter Valid Amount")
            }
        }
    }

    fun userSuccess(res: PrepareAmount) {
        dialog?.dismiss()
        val amount:String = res.data.total_amount.toString()
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra("AMOUNT", amount)
        startActivity(intent)

    }

    fun userFailure(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }

}