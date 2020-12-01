package com.example.mealplan2.controller

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.adapters.NewLoginAdapter
import com.example.mealplan2.views.NewLoginActivity
import org.json.JSONArray
import org.json.JSONObject

class NewLoginController {

    companion object {
        var getdata = mutableListOf<HashMap<String, String>>()
        var role = ""
    }

    var url = "http://25.108.237.40/php_android/get_all_users.php"
    var url2 = "http://25.108.237.40/php_android/insert_user.php"
    var url3 = "http://25.108.237.40/php_android/delete_user.php"
    var url4 = "http://25.108.237.40/php_android/get_all_roles.php"
    var url5 = "http://25.108.237.40/php_android/get_selected_role.php"

//    var url = "http://192.168.1.7/meal_plan2/get_all_users.php"
//    var url2 = "http://192.168.1.7/meal_plan2/insert_user.php"
//    var url3 = "http://192.168.1.7/meal_plan2/delete_user.php"
//    var url4 = "http://192.168.1.7/meal_plan2/get_all_roles.php"
//    var url5 = "http://192.168.1.7/meal_plan2/get_selected_role.php"

    fun showDataMhs(context: Context, adapter: NewLoginAdapter) {
        val request = StringRequest(
            Request.Method.POST, url,
            { response ->
                getdata.clear()
                try {
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("user_name", jsonObject.getString("user_name"))
                        mhs.put("user_cpf", jsonObject.getString("user_cpf"))
                        mhs.put("role_name", jsonObject.getString("role_name"))
                        mhs["user_id"] = jsonObject.getString("user_id")

                        getdata.add(mhs)
                        adapter.notifyDataSetChanged()
                    }


                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Não há Usuários Cadastrados",
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

    fun insertUser(
        nome: String,
        cpf: String,
        pass: String,
        roleid: String,
        context: Context,
        adapter: NewLoginAdapter
    ) {
        val request = object : StringRequest(
            Method.POST, url2,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(
                        context,
                        "Usuário Inserido com Sucesso",
                        Toast.LENGTH_LONG
                    ).show()
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
                hm.put("user_name", nome)
                hm.put("user_cpf", cpf)
                hm.put("user_pass", pass)
                hm.put("role_id", roleid)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun deleteUser(id: String, context: Context, adapter: NewLoginAdapter) {
        val request = object : StringRequest(Method.POST, url3,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(context, "Usuário Excluído", Toast.LENGTH_LONG).show()
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
                hm["user_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun selectedRole(roleName: String, context: Context) {

        val request = object : StringRequest(Method.POST, url5,
            Response.Listener { response ->

                try {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        role = jsonObject.getString("role_id")
                        NewLoginActivity.roleAct = role
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Não há Usuários Cadastrados",
                        Toast.LENGTH_LONG
                    ).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["role_name"] = roleName
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)

    }

    fun getRoles(
        context: Context,
        adapter: NewLoginAdapter,
        spinner: Spinner,
        listSpinner: ArrayList<String>
    ) {
        val request = StringRequest(
            Request.Method.POST, url4,
            { response ->
                try {
                    val jsonArray = JSONArray(response)

                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)

                        listSpinner.add(jsonObject.getString("role_name"))


                    }
                    var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
                        context, R.layout.simple_spinner_item, listSpinner
                    )
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapterSpinner
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Não há Usuários Cadastrados",
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
}