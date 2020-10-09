package com.example.mealplan2.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.R
import com.example.mealplan2.adapters.HistAdapter
import kotlinx.android.synthetic.main.activity_history.*
import org.json.JSONArray

class HistoryActivity : AppCompatActivity() {

    var getdata = mutableListOf<HashMap<String, String>>()
    var url = "http://192.168.1.2/meal_plan2/show_data2.php"
    lateinit var mhsAdapter: HistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        rv_hist.layoutManager = LinearLayoutManager(this)
        rv_hist.setHasFixedSize(true)
        mhsAdapter = HistAdapter(getdata)
        rv_hist.adapter = mhsAdapter
        showDataMhs()

    }

    fun showDataMhs() {
        val request = StringRequest(
            Request.Method.POST, url,
            { response ->
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    var mhs = HashMap<String, String>()
                    mhs.put("table_number", jsonObject.getString("table_number"))
                    mhs.put("order_description", jsonObject.getString("order_description"))
                    mhs.put("order_price", jsonObject.getString("order_price"))
                    mhs.put("order_date", jsonObject.getString("order_date"))
                    mhs.put("order_time", jsonObject.getString("order_time"))
                    getdata.add(mhs)
                }
                mhsAdapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}