package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlin.models.PayResponse
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = SpotsDialog.Builder().setContext(this@MainActivity).build()

        textName.text = "Name: \n${XpayUtils.user!!.name}"
        textEmail.text = "Email: \n${XpayUtils.user!!.email}"
        txtPhone.text = "Phone: \n+2${XpayUtils.user?.phone}"
        txtCommunity.text = "Connunity ID: \n${XpayUtils.communityId}"
        textVariableID.text = "Variable ID: \n${XpayUtils.variableAmountID}"
        txtMethod.text = "Payment Method: \n${XpayUtils.payUsing}"
        totalAmount.text = "Total Amount: \n${XpayUtils.amount}"

        doneButton.setOnClickListener {
            val totalAmount = getIntent().getStringExtra("TOTAL_AMOUNT")?.toDouble()
            if (totalAmount != null) {
                dialog!!.show()
                XpayUtils.pay(::userSuccess, ::userFailure)
            }
        }

    }

    fun userSuccess(res: PayResponse) {
        dialog?.dismiss()
        XpayUtils.iframeUrl = res.data.iframe_url
        if (res.data.iframe_url != null) {
            startActivity(Intent(this, PayActivity::class.java))
        } else if (res.data.message != null) {
            val intent = Intent(baseContext, PayActivity::class.java)
            intent.putExtra("UUID", res.data.transaction_uuid)
            intent.putExtra("MESSAGE", res.data.message)
            startActivity(intent)
        }
    }

    fun userFailure(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }
}
