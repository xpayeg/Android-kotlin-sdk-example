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
            val totalAmount = getIntent().getStringExtra("TOTAL_AMOUNT")?.toDouble()
            if (totalAmount != null) {
                dialog!!.show()
                XpayUtils.pay( String.format("%.2f", totalAmount).toDouble(),XpayUtils.apiKey!!,::userSuccess,::userFailure)
            }
        }

    }
    fun userSuccess(res: PayResponse) {
        XpayUtils.iframeUrl=res.data.iframe_url
        startActivity(Intent(this, PayActivity::class.java))
        dialog?.dismiss()
    }

    fun userFailure(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }
}
