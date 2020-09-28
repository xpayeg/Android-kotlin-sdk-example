package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.xpay.kotlin.models.PrepareAmount
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.model.CustomField
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject


class Login : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var sharedPreferences: SharedPreferences? = null
    var customFields = mutableListOf<CustomField>()

    override fun onCreate(savedInstanceState: Bundle?) {
        XpayUtils.apiKey = "3uBD5mrj.3HSCm46V7xJ5yfIkPb2gBOIUFH4Ks0Ss"
        XpayUtils.communityId = "zogDmQW"
//        XpayUtils.currency = "EGP"  // private field
        XpayUtils.payUsing = "card"
        XpayUtils.variableAmountID = 18
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        customFields.add( CustomField("Name","Ahmed"))
        customFields.add(CustomField("Age","6"))
        customFields.add(CustomField("Gender","Male"))
        sharedPreferences =
            this.getSharedPreferences("save", Context.MODE_PRIVATE)

        dialog = SpotsDialog.Builder().setContext(this@Login).build()

        btnLogin.setOnClickListener {
            val num: Number?
            if (userAmount.text.toString().isNotEmpty()) {
                num=userAmount.text.toString().toDouble()
                if(txt_field_label.text.toString().isNotEmpty() ){
                    XpayUtils.addCustomField("field_labe l",txt_field_label.text.toString())
                }

                if(txt_field_value.text.toString().isNotEmpty() ){
                    XpayUtils.addCustomField("field_labe 2",txt_field_value.text.toString())
                }
                dialog?.show()
                XpayUtils.prepareAmount(num, ::userSuccess, ::userFailure)
            } else {
                userAmount.setError("Enter Valid Amount")
            }
        }
    }

    fun userSuccess(res: PrepareAmount) {
        dialog?.dismiss()
        Toast.makeText(this,res.data.total_amount.toString(),Toast.LENGTH_LONG).show()
        Log.i("TAG", "totalAmount: ${XpayUtils.totalAmount?.cash}\n ${XpayUtils.totalAmount?.card}\n ${XpayUtils.totalAmount?.kiosk}")
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