package com.xpay.kotlin

import android.content.Context
import android.content.Intent
import android.icu.text.IDNA
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.PaymentMethods
import com.xpay.kotlinutils.models.ShippingInfo
import com.xpay.kotlinutils.models.User
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class UserActivity : AppCompatActivity() {
    var amount: Number = 0
    private var mSpinnerData: MutableList<PaymentMethods> = XpayUtils.activePaymentMethods
    private var mStateData: MutableList<String>? = null
    var adapter: ArrayAdapter<String>? = null
    var adapterCountry: ArrayAdapter<String>? = null
    var validForm: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val paymentMethodsList : MutableList<String> = mutableListOf<String>()

        for(i in mSpinnerData){
            paymentMethodsList.add(i.toString())
        }

        adapter = paymentMethodsList.toList().let {
            ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, it
            )
        }

        spinner.adapter =adapter


        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (XpayUtils.activePaymentMethods[position]) {
                    PaymentMethods.CASH -> {
                        amount = XpayUtils.PaymentOptionsTotalAmounts?.cash!!;
                        XpayUtils.payUsing = PaymentMethods.CASH
                        showView(constraint_shipping)
                    }
                    PaymentMethods.CARD -> {
                        amount = XpayUtils.PaymentOptionsTotalAmounts?.card!!;
                        XpayUtils.payUsing =PaymentMethods.CARD
                        hideView(constraint_shipping);
                        validForm = true
                    }
                    PaymentMethods.KIOSK -> {
                        amount = XpayUtils.PaymentOptionsTotalAmounts?.kiosk!!;
                        XpayUtils.payUsing =PaymentMethods.KIOSK
                        hideView(constraint_shipping);
                        validForm = true
                    }
                }
                amount = String.format("%.2f", amount).toDouble()
                totalAmountTxt.text = "Total Amount: ${amount} Egp"
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        val jsonFileString = getJsonDataFromAsset(applicationContext, "countries.json")
        val obj = JSONObject(jsonFileString!!)
        val allCountries =
            ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.countries)))

        sp_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                populateState(obj, allCountries[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }


        btnSubmit.setOnClickListener {
            val fullName: String = userName2.text.toString()
            val email: String = userEmail.text.toString()
            val phone = "${userPhone.text}"
            val street = et_street.text.toString()
            val building = et_building.text.toString()
            val apartment = et_apartment.text.toString()
            val floor = et_floor.text.toString()

            if (constraint_shipping.visibility == View.VISIBLE) {
                if (street.isNotEmpty() && building.isNotEmpty() && apartment.isNotEmpty() && floor.isNotEmpty()) {
                    validForm = true
                    XpayUtils.shippingShippingInfo = ShippingInfo(
                        "EG",
                        sp_state.selectedItem.toString(),
                        sp_country.selectedItem.toString(),
                        et_apartment.text.toString(),
                        et_building.text.toString(),
                        et_floor.text.toString(),
                        et_street.text.toString()
                    )
                } else {
                    validForm = false
                    if (street.isEmpty()) {
                        et_street.setError("Enter valid street no")
                    }
                    if (building.isEmpty()) {
                        et_building.setError("Enter valid building no")
                    }
                    if (apartment.isEmpty()) {
                        et_apartment.setError("Enter valid apartment no")
                    }
                    if (floor.isEmpty()) {
                        et_floor.setError("Enter valid floor no")
                    }
                }
            }

            if (fullName.isNotEmpty() && email.isEmailValid() && phone.length >= 9 && validForm) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT",amount.toString())
                startActivity(intent)
                XpayUtils.userInfo = User(fullName, email, phone)
            } else {
                if (fullName.isEmpty())
                    userName2.setError("Enter valid Full Name")
                if (!email.isEmailValid())
                    userEmail.setError("Enter valid Email")
                if (phone.isEmpty() || phone.length < 9)
                    userPhone.setError("Enter valid Phone Number")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mSpinnerData.clear()

    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun populateState(obj: JSONObject, key: String) {
        val sessionArray: JSONArray = obj.optJSONArray(key)!!
        val list = ArrayList<String>()
        for (i in 0 until sessionArray.length()) {
            list.add(sessionArray.getString(i))
        }
        mStateData = list
        adapterCountry = mStateData?.let {
            ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, it
            )
        }
        adapterCountry?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_state.adapter = adapterCountry
    }

    fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hideView(view: View) {
        view.visibility = View.GONE
    }
}