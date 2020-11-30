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
import kotlinx.android.synthetic.main.activity_payment_preview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PaymentPreviewActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null
    private var isCardPayment = false
    var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_preview)
        dialog = SpotsDialog.Builder().setContext(this@PaymentPreviewActivity).build()

        // 01-start
        textName.text = "Name: \n${XpayUtils.billingInfo!!.name}"
        textEmail.text = "Email: \n${XpayUtils.billingInfo!!.email}"
        txtPhone.text = "Phone: \n${XpayUtils.billingInfo?.phone}"
        txtMethod.text = "Payment Method: \n${XpayUtils.payUsing}"
        totalAmount.text = "Total Amount: \n${intent.getStringExtra("TOTAL_AMOUNT")?.toDouble()}"
        // 01-end

        // Confirm button method
        Confirmbtn.setOnClickListener {
            // 02-start
            try {
                dialog!!.show()
                GlobalScope.launch {
                    val response = XpayUtils.pay()
                    response?.let { completePayment(response) }
                }
            } catch (e: Exception) {
                dialog?.dismiss()
                e.message?.let { it1 -> Toast.makeText(this, it1, Toast.LENGTH_LONG).show() }
            }
            // 02-end
        }

    }

    // when Payment successful
    private fun completePayment(response: PayData) {
        dialog?.dismiss()
        // 03-start
        if (response.iframe_url != null) {
            // if iframe_url inside the returned response is not null, launch a web view to display the payment form
            isCardPayment = true
            uuid = response.transaction_uuid
            // start a web view and navigate the user to the credit card form
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
            builder.setShowTitle(true)
            val customTabsIntent: CustomTabsIntent = builder.build()
            customTabsIntent.launchUrl(this@PaymentPreviewActivity, Uri.parse(response.iframe_url))
        } else if (response.message != null) {
            // if iframe_url inside the returned response is null while the message is not null
            // this is a kiosk or cash collection payment, just display the message to the user
            isCardPayment = false
            val intent = Intent(baseContext, CashPaymentActivity::class.java)
            intent.putExtra("UUID", response.transaction_uuid)
            intent.putExtra("MESSAGE", response.message)
            startActivity(intent)
        }
        // 03-end
    }

    // method to handle web view dismiss case
    // when the user dismisses the web view then navigate to transaction activity which shows him the transaction info
    override fun onRestart() {
        super.onRestart()
        // 04-start
        if (isCardPayment) {
            val intent = Intent(this, TransactionActivity::class.java)
            intent.putExtra("UUID", uuid)
            startActivity(intent)
        }
        // 04-end
    }
}
