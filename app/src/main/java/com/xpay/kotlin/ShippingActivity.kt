package com.xpay.kotlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.model.Info
import kotlinx.android.synthetic.main.activity_shipping.*
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ShippingActivity : AppCompatActivity() {
    private var mSpinnerData: MutableList<String>? = null
    var adapter: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)
        val mActionBarToolbar: Toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar()?.setTitle("Shipping Info");

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

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
        btn_info_submit.setOnClickListener {
            XpayUtils.shippingInfo = Info(
                "EG",
                sp_state.selectedItem.toString(),
                sp_country.selectedItem.toString(),
                et_apartment.text.toString(),
                et_building.text.toString(),
                et_floor.text.toString(),
                et_street.text.toString()
            )
            onBackPressed()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
        mSpinnerData = list
        adapter = mSpinnerData?.let {
            ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, it
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_state.adapter = adapter
    }

}