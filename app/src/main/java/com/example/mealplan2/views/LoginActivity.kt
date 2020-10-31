package com.example.mealplan2.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.R
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    companion object {
        var currentUser: String = ""
        var currentPrivilege: String = ""
        private var currentPass: String = ""
    }

    var url = "http://192.168.0.22/php_android/get_current_user.php"

//    var url = "http://192.168.1.2/meal_plan2/get_current_user.php"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            if (cpf.text.isEmpty() || password.text.isEmpty()) {
                cpf.error = "Preencha os valores"
                password.error = "Preencha os valores"
            } else {
                val cpf = cpf.text.toString().trim()
                val pass = password.text.toString().trim()

                getData(cpf, pass)
            }
        }

    }

    fun getData(cpf: String, pass: String) {
        val request = object : StringRequest(Method.POST, url,
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
                        val intent = Intent(this, LoggedActivity::class.java)
                        startActivity(intent)
                    } else {

                        Toast.makeText(applicationContext, "Usuário ou Senha Incorretos", Toast.LENGTH_LONG)
                            .show()
                    }


                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Usuário ou Senha Incorretos",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm.put("rec_cpf", cpf)
                hm.put("rec_pass", pass)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}