package com.example.mealplan2.controller

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.adapters.PedidosAdapter
import org.json.JSONArray
import org.json.JSONObject

class PedidosController {

    companion object {
        var getdata = mutableListOf<HashMap<String, String>>()
    }

    //    var url = "http://192.168.0.22/php_android/show_orders.php"
    //    var url2 = "http://192.168.0.22/php_android/delete_order.php"
    //    var url3 = "http://192.168.0.22/php_android/update_order.php"
    //    var url4 = "http://192.168.0.22/php_android/finish_order.php"

    var url = "http://192.168.1.2/meal_plan2/show_orders.php"
    var url2 = "http://192.168.1.2/meal_plan2/delete_order.php"
    var url3 = "http://192.168.1.2/meal_plan2/update_order.php"
    var url4 = "http://192.168.1.2/meal_plan2/finish_order.php"

    fun showDataMhs(context: Context, adapter: PedidosAdapter) {
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
                        mhs.put("order_id", jsonObject.getString("order_id"))
                        getdata.add(mhs)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Não há pedidos em Andamento",
                        Toast.LENGTH_LONG
                    ).show()
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun deleteOrder(id: String, context: Context, adapter: PedidosAdapter) {
        val request = object : StringRequest(Method.POST, url2,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Pedido Excluído", Toast.LENGTH_LONG).show()
                    showDataMhs(context, adapter)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["order_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }


    fun finishOrder(id: String, context: Context, adapter: PedidosAdapter) {
        val request = object : StringRequest(Method.POST, url4,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Pedido Finalizado", Toast.LENGTH_LONG)
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
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["order_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun updateOrder(id: String, desc: String, context: Context, adapter: PedidosAdapter) {
        val request = object : StringRequest(Method.POST, url3,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Pedido Alterado", Toast.LENGTH_LONG).show()
                    showDataMhs(context, adapter)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["order_id"] = id
                hm.put("order_description", desc)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
}