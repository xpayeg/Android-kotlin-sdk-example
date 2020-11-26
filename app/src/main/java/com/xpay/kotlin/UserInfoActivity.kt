package com.xpay.kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.BillingInfo
import com.xpay.kotlinutils.models.PaymentMethods
import com.xpay.kotlinutils.models.ShippingInfo
import kotlinx.android.synthetic.main.activity_user_info.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList


class UserInfoActivity : AppCompatActivity() {

    var totalAmount: Number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: 2020-11-17 check if needed
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        // Populate paymentMethodsDropdown with available active payment methods
        val paymentMethodsAdapter: ArrayAdapter<String>?
        val paymentMethodsList: MutableList<String> = mutableListOf()
        // get the available active payment methods and convert it to List<String>
        for (paymentMethod in XpayUtils.activePaymentMethods) {
            paymentMethodsList.add(paymentMethod.toString())
        }
        paymentMethodsAdapter = paymentMethodsList.distinct().toList().let {
            ArrayAdapter(
                this, android.R.layout.simple_spinner_item, it
            )
        }
        paymentMethodsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paymentMethodsDropdown.adapter = paymentMethodsAdapter

        // set actual amount for different payment methods
        paymentMethodsDropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (XpayUtils.activePaymentMethods[position]) {
                    PaymentMethods.CASH -> {
                        totalAmount = XpayUtils.PaymentOptionsTotalAmounts?.cash!!
                        XpayUtils.payUsing = PaymentMethods.CASH
                        showView(constraint_shipping)
                    }
                    PaymentMethods.CARD -> {
                        totalAmount = XpayUtils.PaymentOptionsTotalAmounts?.card!!
                        XpayUtils.payUsing = PaymentMethods.CARD
                        hideView(constraint_shipping)
                    }
                    PaymentMethods.KIOSK -> {
                        totalAmount = XpayUtils.PaymentOptionsTotalAmounts?.kiosk!!
                        XpayUtils.payUsing = PaymentMethods.KIOSK
                        hideView(constraint_shipping)
                    }
                }
                totalAmount = String.format("%.2f", totalAmount).toDouble()
                totalAmountTxt.text = "Total Amount: ${totalAmount} Egp"
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        // populate country list

        // get the value of countries-cities combinations from assets
        val jsonFileString = getJsonDataFromAsset(applicationContext, "countries.json")
        val obj = JSONObject(jsonFileString!!)

        val countriesList = populateCountries(obj)

        // when a country is selected, populate its cities
        sp_country.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                populateStates(obj, countriesList[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        // submit button method
        btnSubmit.setOnClickListener {
            // validate shipping info(in case cash collection method is selected)
            var validShippingInfo: Boolean = true
            if (constraint_shipping.visibility == View.VISIBLE) {
                if (validateShippingInfo()) {
                    validShippingInfo = true
                    // set payment shipping info
                    XpayUtils.ShippingInfo = ShippingInfo(
                        "EG",
                        sp_state.selectedItem.toString(),
                        sp_country.selectedItem.toString(),
                        et_apartment.text.toString(),
                        et_building.text.toString(),
                        et_floor.text.toString(),
                        et_street.text.toString()
                    )
                } else validShippingInfo = false
            }

            if (validateBillingInfo() && validShippingInfo) {
                // set payment billing info
                try {
                    XpayUtils.billingInfo =
                        BillingInfo(
                            userName.text.toString(),
                            userEmail.text.toString(),
                            "+2${userPhone.text}"
                        )
                    val intent = Intent(this, PaymentPreviewActivity::class.java)
                    intent.putExtra("TOTAL_AMOUNT", totalAmount.toString())
                    startActivity(intent)
                } catch (e: Exception) {
                    e.message?.let { it1 -> Toast.makeText(this, it1, Toast.LENGTH_LONG).show() }
                }

            }
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    // populate countries dropdown list and return a list of countries
    fun populateCountries(obj: JSONObject): ArrayList<String> {
        var adapter: ArrayAdapter<String>? = null
        val countriesList = ArrayList<String>()
        for (i in obj.keys()) {
            countriesList.add(i)
        }
        adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, countriesList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_country.adapter = adapter
        return countriesList
    }

    // populate cities dropdown list
    fun populateStates(obj: JSONObject, key: String) {
        var adapter: ArrayAdapter<String>? = null
        val citiesArray: JSONArray = obj.optJSONArray(key)!!
        val citiesList = ArrayList<String>()
        for (i in 0 until citiesArray.length()) {
            citiesList.add(citiesArray.getString(i))
        }
        adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, citiesList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_state.adapter = adapter
    }

    // Show shipping info form
    fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    // Hide shipping info form
    fun hideView(view: View) {
        view.visibility = View.GONE
    }

    // validate shipping info form
    fun validateShippingInfo(): Boolean {
        // get shipping info
        val street = et_street.text.toString()
        val building = et_building.text.toString()
        val apartment = et_apartment.text.toString()
        val floor = et_floor.text.toString()

        return if (street.isNotEmpty() && building.isNotEmpty() && apartment.isNotEmpty() && floor.isNotEmpty()) {
            true
        } else {
            if (street.isEmpty()) {
                et_street.error = "Enter valid street name"
            }
            if (building.isEmpty()) {
                et_building.error = "Enter valid building number"
            }
            if (apartment.isEmpty()) {
                et_apartment.error = "Enter valid apartment number"
            }
            if (floor.isEmpty()) {
                et_floor.error = "Enter valid floor number"
            }
            false
        }
    }

    // validate billing info form
    fun validateBillingInfo(): Boolean {
        // validate billing info
        val fullName: String = userName.text.toString()
        val email: String = userEmail.text.toString()
        val phone = "+2${userPhone.text}"

        return if (fullName.isNotEmpty() && email.isEmailValid() && phone.length >= 9) {
            true
        } else {
            if (fullName.isEmpty())
                userName.error = "Enter valid Full Name"
            if (!email.isEmailValid())
                userEmail.error = "Enter valid Email"
            if (phone.isEmpty() || phone.length < 9)
                userPhone.error = "Enter valid Phone Number"
            false
        }
    }
}
