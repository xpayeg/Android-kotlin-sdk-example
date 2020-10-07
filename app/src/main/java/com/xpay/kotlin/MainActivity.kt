package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlin.models.PayData
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null

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
        totalAmount.text="Total Amount: \n${intent.getStringExtra("TOTAL_AMOUNT")?.toDouble()}"

        doneButton.setOnClickListener {
            val totalAmount = intent.getStringExtra("TOTAL_AMOUNT")?.toDouble()
            if (totalAmount != null) {
                dialog!!.show()
                XpayUtils.pay(::userSuccess, ::userFailure)
            }
        }

    }

    fun userSuccess(res: PayData) {
        dialog?.dismiss()
        val intent = Intent(baseContext, PayActivity::class.java)
//        XpayUtils.iframeUrl = res.iframe_url
        if (res.iframe_url != null) {
            intent.putExtra("URL", res.iframe_url)
            startActivity(intent)
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
}
