package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.api.prepare.PrepareAmountData
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
        uuid = intent.getStringExtra("UUID")
        println("""UUID: ${uuid}""")
        trans_btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun loadTransaction(Uid:String){
        try {
            lifecycleScope.launch {
                Uid.let { XpayUtils.getTransaction(it) }
                dialog?.dismiss()
            }
        } catch (e: Exception) {
            println("""TransactionDataError: ${uuid}""")
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
//        }
        }
    }
    override fun onStart() {
        super.onStart()
        uuid?.let { loadTransaction(it) }
    }

    private fun updateTransaction(res: TransactionData) {
        txt_trans_status.text = """${res.status} TRANSACTION"""
        txt_trans_accessor.text = res.payment_for
        txt_status_egp.text = """${res.total_amount} EGP"""
        dialog?.dismiss()
    }
}