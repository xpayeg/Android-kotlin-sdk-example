package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.api.transaction.TransactionData
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.coroutines.launch

class TransactionActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        dialog = SpotsDialog.Builder().setContext(this@TransactionActivity).build()

        // get Transaction UUID value
        uuid = intent.getStringExtra("UUID")

        // go to login screen when done button is pressed
        trans_btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    // Load transaction when activity launches
    override fun onStart() {
        super.onStart()
        uuid?.let { loadTransaction(it) }
    }

    private fun loadTransaction(uuid: String) {
        lifecycleScope.launch {
            try {
                dialog?.show()
                uuid.let { it ->
                    val response = XpayUtils.getTransaction(it)
                    response?.let { updateTransactionInfo(it) }
                }
            } catch (e: Exception) {
                dialog?.hide()
                e.message?.let { it1 -> displayError(it1) }
            }

        }
    }

    private fun updateTransactionInfo(tx: TransactionData) {
        txt_trans_status.text = """${tx.status} TRANSACTION"""
//        txt_trans_accessor.text = tx.payment_for
        txt_status_egp.text = """${tx.total_amount} EGP"""
        dialog?.dismiss()
    }

    private fun displayError(message: String) {
        dialog?.dismiss()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}