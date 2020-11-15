package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.api.pay.PayData
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var builder = CustomTabsIntent.Builder()
    var isCard = false
    var uuid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = SpotsDialog.Builder().setContext(this@MainActivity).build()

        textName.text = "Name: \n${XpayUtils.userInfo!!.name}"
        textEmail.text = "Email: \n${XpayUtils.userInfo!!.email}"
        txtPhone.text = "Phone: \n${XpayUtils.userInfo?.phone}"
        txtCommunity.text = "Connunity ID: \n${XpayUtils.communityId}"
        textVariableID.text = "Variable ID: \n${XpayUtils.variableAmountID}"
        txtMethod.text = "Payment Method: \n${XpayUtils.payUsing}"
        totalAmount.text = "Total Amount: \n${intent.getStringExtra("TOTAL_AMOUNT")?.toDouble()}"

        doneButton.setOnClickListener {
            val totalAmount = intent.getStringExtra("TOTAL_AMOUNT")?.toDouble()
            if (totalAmount != null) {
                try {
                    dialog!!.show()
                    GlobalScope.launch {
                        val payRes = XpayUtils.pay()
                        payRes?.let { userSuccess(payRes) }
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> userFailure(it1) }
                }
            }
        }

    }

    private fun userSuccess(res: PayData) {
        dialog?.dismiss()
        val intent = Intent(baseContext, PayActivity::class.java)
        if (res.iframe_url != null) {
            isCard = true
            uuid = res.transaction_uuid
            builder.setToolbarColor( resources.getColor(R.color.colorPrimary))
            builder.setShowTitle(true)
            val customTabsIntent: CustomTabsIntent = builder.build()
            customTabsIntent.launchUrl(this@MainActivity, Uri.parse(res.iframe_url))
        } else if (res.message != null) {
            intent.putExtra("UUID", res.transaction_uuid)
            intent.putExtra("MESSAGE", res.message)
            startActivity(intent)
        }

    }

    fun userFailure(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }

    private fun goHome() {
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        if (isCard) {
            goHome()
        }
    }
}
