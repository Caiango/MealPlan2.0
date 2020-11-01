package com.example.mealplan2.controller

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.adapters.PratosAdapter
import org.json.JSONArray
import org.json.JSONObject

class PratosController {

    companion object {
        var getdata = mutableListOf<HashMap<String, String>>()
    }
    
//    var url = "http://192.168.0.22/php_android/show_pratos.php"
//    var url2 = "http://192.168.0.22/php_android/get_categories.php"
//    var url3 = "http://192.168.0.22/php_android/insert_prato.php"
//    var url4 = "http://192.168.0.22/php_android/delete_prato.php"
//    var url5 = "http://192.168.0.22/php_android/update_prato.php"
//    var url6 = "http://192.168.0.22/php_android/select_by_category.php"

    var url = "http://192.168.1.2/meal_plan2/show_pratos.php"
    var url2 = "http://192.168.1.2/meal_plan2/get_categories.php"
    var url3 = "http://192.168.1.2/meal_plan2/insert_prato.php"
    var url4 = "http://192.168.1.2/meal_plan2/delete_prato.php"
    var url5 = "http://192.168.1.2/meal_plan2/update_prato.php"
    var url6 = "http://192.168.1.2/meal_plan2/select_by_category.php"

    fun showDataMhs(context: Context, adapter: PratosAdapter) {
        val request = StringRequest(
            Request.Method.POST, url,
            { response ->
                getdata.clear()
                try {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("food_name", jsonObject.getString("food_name"))
                        mhs.put("food_description", jsonObject.getString("food_description"))
                        mhs.put("food_price", jsonObject.getString("food_price"))
                        mhs.put("food_id", jsonObject.getString("food_id"))
                        getdata.add(mhs)
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Não há Pratos Cadastrados",
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

    fun deleteFood(id: String, context: Context, adapter: PratosAdapter) {
        val request = object : StringRequest(Method.POST, url4,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Prato Excluído", Toast.LENGTH_LONG).show()
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
                hm["food_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun getPratosByCat(cat: String, context: Context, adapter: PratosAdapter) {
        val request = object : StringRequest(Method.POST, url6,
            Response.Listener { response ->
                getdata.clear()

                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    var mhs = HashMap<String, String>()
                    mhs.put("food_name", jsonObject.getString("food_name"))
                    mhs.put("food_description", jsonObject.getString("food_description"))
                    mhs.put("food_price", jsonObject.getString("food_price"))
                    mhs.put("food_id", jsonObject.getString("food_id"))
                    getdata.add(mhs)
                }
                adapter.notifyDataSetChanged()

            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["category_name"] = cat
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun insertFood(
        nome: String,
        desc: String,
        valor: String,
        cat: String,
        context: Context,
        adapter: PratosAdapter
    ) {
        val request = object : StringRequest(Method.POST, url3,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Prato Inserido", Toast.LENGTH_LONG).show()
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

                if (cat == "Almoço") {
                    hm.put("category_id", "1")
                } else if (cat == "Lanche") {
                    hm.put("category_id", "2")
                } else if (cat == "Jantar") {
                    hm.put("category_id", "3")
                } else if (cat == "Sobremesa") {
                    hm.put("category_id", "4")
                }

                //recebendo e enviando valores para o php
                hm.put("food_name", nome)
                hm.put("food_description", desc)
                hm.put("food_price", valor)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun updateFood(
        id: String,
        nome: String,
        desc: String,
        valor: String,
        cat: String,
        context: Context,
        textView: TextView,
        adapter: PratosAdapter
    ) {
        val request = object : StringRequest(Method.POST, url5,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Prato Alterado", Toast.LENGTH_LONG).show()
                    showDataMhs(context, adapter)
                    adapter.notifyDataSetChanged()
                    textView.text = "Todos"
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
                hm["food_id"] = id
                hm.put("food_name", nome)
                hm.put("food_description", desc)
                hm.put("food_price", valor)

                if (cat == "Almoço") {
                    hm.put("cat_id", "1")

                } else if (cat == "Lanche") {
                    hm.put("cat_id", "2")

                } else if (cat == "Jantar") {
                    hm.put("cat_id", "3")

                } else if (cat == "Sobremesa") {
                    hm.put("cat_id", "4")

                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun getCategories(context: Context, spinner: Spinner, listSpinner: ArrayList<String>) {
        listSpinner.clear()
        val request = StringRequest(
            Request.Method.POST, url2,
            { response ->

                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(x)

                    //adicionando as categorias obtidas no banco dentro de uma lista
                    listSpinner.add(jsonObject.getString("category_name"))


                }
                //criando variável adapter que vai receber os dados da lista
                var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
                    context, R.layout.simple_spinner_item, listSpinner
                )
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                //setando adapter criado no meu spinner
                spinner.adapter = adapterSpinner

            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
}