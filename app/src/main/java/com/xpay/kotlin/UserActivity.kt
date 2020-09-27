package com.xpay.kotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlin.models.User
import com.xpay.kotlinutils.XpayUtils
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity() {
    private var mSpinnerData: MutableList<String>? = XpayUtils.paymentOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val adapter: ArrayAdapter<String>? = mSpinnerData?.let {
            ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, it
            )
        }
        var amount: Number = 0
        if (adapter != null) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        };
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (XpayUtils.paymentOptions[position]) {
                    "CASH" -> {
                        amount = XpayUtils.totalAmount?.cash!!; XpayUtils.payUsing =
                            "cash";showView(btnShippingInfo)
                    }
                    "CARD" -> {
                        amount = XpayUtils.totalAmount?.card!!; XpayUtils.payUsing =
                            "card"; hideView(btnShippingInfo)
                    }
                    "KIOSK" -> {
                        amount = XpayUtils.totalAmount?.kiosk!!; XpayUtils.payUsing =
                            "kiosk"; hideView(btnShippingInfo)
                    }
                }
                totalAmountTxt.setText("total amount: ${amount} Egp")
                XpayUtils.amount = String.format("%.2f", amount).toDouble()
//                Toast.makeText(applicationContext, String.format("%.2f", amount).toDouble().toString(), Toast.LENGTH_LONG).show()
                // your code here
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        btnShippingInfo.setOnClickListener {
            val intent = Intent(this, ShippingActivity::class.java)
            startActivity(intent)
        }


        btnSubmit.setOnClickListener {
            val fullName: String = userName2.text.toString()
            val email: String = userEmail.text.toString()
            val phone = "${userPhone.text}"

            if (fullName.isNotEmpty() && email.isEmailValid() && phone.length == 11) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", getIntent().getStringExtra("AMOUNT"))
                startActivity(intent)
                XpayUtils.user = User(fullName, email, phone)
            } else {
                if (fullName.isEmpty())
                    userName2.setError("Enter valid Full Name")
                if (!email.isEmailValid())
                    userEmail.setError("Enter valid Email")
                if (phone.isEmpty() || phone.length < 11)
                    userPhone.setError("Enter valid Phone Number")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mSpinnerData?.clear()

    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hideView(view: View) {
        view.visibility = View.GONE
    }
}