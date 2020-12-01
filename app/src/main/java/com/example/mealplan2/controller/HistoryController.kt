package com.example.mealplan2.controller

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.adapters.HistAdapter
import org.json.JSONArray
import org.json.JSONObject

class HistoryController {

    companion object {
        var getdata = mutableListOf<HashMap<String, String>>()
    }

    var url = "http://25.108.237.40/php_android/show_history.php"
    var url2 = "http://25.108.237.40/php_android/delete_all_hist.php"

//    var url = "http://192.168.1.7/meal_plan2/show_history.php"
//    var url2 = "http://192.168.1.7/meal_plan2/delete_all_hist.php"

    fun showDataMhs(context: Context, adapter: HistAdapter) {
        val request = StringRequest(
            Request.Method.POST, url,
            { response ->
                getdata.clear()
                try {

                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("table_number", jsonObject.getString("table_number"))
                        mhs.put("order_description", jsonObject.getString("order_description"))
                        mhs.put("order_price", jsonObject.getString("order_price"))
                        mhs.put("order_date", jsonObject.getString("order_date"))
                        mhs.put("order_time", jsonObject.getString("order_time"))
                        mhs.put("client_name", jsonObject.getString("client_name"))
                        getdata.add(mhs)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Não há histórico", Toast.LENGTH_LONG).show()
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun deleteHist(context: Context, adapter: HistAdapter) {
        val request = object : StringRequest(Method.POST, url2,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Histórico Excluído", Toast.LENGTH_LONG)
                        .show()
                    showDataMhs(context, adapter)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

}