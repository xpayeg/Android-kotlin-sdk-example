package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.xpay.kotlin.databinding.ActivityMainBinding
import com.xpay.kotlin.models.PayResponse
import com.xpay.kotlin.models.PrepareAmount
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var dialog: AlertDialog? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog= SpotsDialog.Builder().setContext(this@MainActivity).build()

          textName.text="Name: \n${XpayUtils.user!!.name}"
          textEmail.text="Email: \n${XpayUtils.user!!.email}"
          textView3.text="Phone: \n+2${XpayUtils.user?.phone}"
          textView4.text="Connunity ID: \n${XpayUtils.communityId}"
          textVariableID.text="Variable ID: \n${XpayUtils.variableAmountID}"

        doneButton.setOnClickListener{
            dialog!!.show()
            XpayUtils.payBill(XpayUtils.amount!!.toDouble(),XpayUtils.apiKey!!,::userSuccess,::userFailure)
//            Handler().postDelayed({
//                dialog!!.dismiss()
//
//            }, 2000);
        }

    }
    fun userSuccess(res: PayResponse) {
        XpayUtils.iframeUrl=res.data.iframe_url
//        webView.loadUrl("https://dev-payment.xpay.app/core/payment_iframe/5556/")
        startActivity(Intent(this, PayActivity::class.java))
//            val editor: SharedPreferences.Editor = sharedPreferences!!.edit
//            ()
        dialog?.dismiss()
//            Toast.makeText(this, res.data.total_amount.toString(), Toast.LENGTH_LONG).show()
//            XpayUtils.amount=userAmount.text.toString().toDouble()
//            startActivity(Intent(this, UserActivity::class.java))
//        editor.putFloat("amount",userAmount.text.toString().toFloat()).apply()

    }

    fun userFailure(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }
}
