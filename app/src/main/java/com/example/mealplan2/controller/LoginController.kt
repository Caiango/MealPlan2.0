package com.example.mealplan2.controller

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.views.LoggedActivity
import org.json.JSONArray

class LoginController {

    companion object {
        var currentUser: String = ""
        var currentPrivilege: String = ""
        private var currentPass: String = ""
    }

//    var url = "http://192.168.1.7/meal_plan2/get_current_user.php"

        var url = "http://25.108.237.40/php_android/get_current_user.php"

    fun getData(cpf: String, pass: String, context: Context) {
        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    var currentCPF = ""

                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)

                        currentUser = jsonObject.getString("user_name")
                        currentCPF = jsonObject.getString("user_cpf")
                        currentPrivilege = jsonObject.getString("role_name")
                        currentPass = jsonObject.getString("user_password")

                    }

                    if (cpf == currentCPF && pass == currentPass) {
                        val intent = Intent(context, LoggedActivity::class.java)
                        startActivity(context, intent, null)
                    } else {
                        Toast.makeText(context, "Usuário ou Senha Incorretos", Toast.LENGTH_LONG)
                            .show()
                    }


                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Usuário ou Senha Incorretos",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm.put("rec_cpf", cpf)
                hm.put("rec_pass", pass)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

}