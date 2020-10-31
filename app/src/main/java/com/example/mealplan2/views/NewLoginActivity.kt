package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.R
import com.example.mealplan2.adapters.NewLoginAdapter
import kotlinx.android.synthetic.main.activity_new_login.*
import kotlinx.android.synthetic.main.activity_pratos.*
import kotlinx.android.synthetic.main.all_users_layout.*
import org.json.JSONArray
import org.json.JSONObject

class NewLoginActivity : AppCompatActivity(), NewLoginAdapter.onLongClickListener {

    var getdata = mutableListOf<HashMap<String, String>>()
//    var url = "http://192.168.0.22/php_android/get_all_users.php"
//    var url2 = "http://192.168.0.22/php_android/insert_user.php"
//    var url3 = "http://192.168.0.22/php_android/delete_user.php"

    var url = "http://192.168.1.2/meal_plan2/get_all_users.php"
    var url2 = "http://192.168.1.2/meal_plan2/insert_user.php"
    var url3 = "http://192.168.1.2/meal_plan2/delete_user.php"

    lateinit var mhsAdapter: NewLoginAdapter

    var privilegesList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_login)

        privilegesList.add("Administração")
        privilegesList.add("Cozinha")
        privilegesList.add("Atendimento")

        var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, privilegesList
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newSpinner.adapter = adapterSpinner

        imageBackNew.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        }

        rvNewUsers.layoutManager = LinearLayoutManager(this)
        rvNewUsers.setHasFixedSize(true)
        mhsAdapter = NewLoginAdapter(getdata, this)
        rvNewUsers.adapter = mhsAdapter

        showDataMhs()

        newUser.setOnClickListener {
            var nome = edtNewUser.text.toString().trim()
            var senha = edtNewPass.text.toString().trim()
            var cpf = edtCPF.text.toString().trim()

            val textSpinner = newSpinner.selectedItem.toString()

            var role_id = ""

            if (textSpinner == "Administração") {
                role_id = "1"
            } else if (textSpinner == "Cozinha") {
                role_id = "2"
            } else {
                role_id = "3"
            }

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Confirma os Dados?")
            dialog.setMessage("Nome: $nome \nCPF: $cpf\nSenha: $senha")
            dialog.setPositiveButton("Sim") { _: DialogInterface, _: Int ->
                insertUser(nome, cpf, senha, role_id)
            }
            dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->
                Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG)
                    .show()
            }
            dialog.show()
        }


    }

    fun showDataMhs() {
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
                        mhs.put("user_id", jsonObject.getString("user_id"))

                        getdata.add(mhs)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Não há Usuários Cadastrados",
                        Toast.LENGTH_LONG
                    ).show()
                }
                mhsAdapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    fun insertUser(nome: String, cpf: String, pass: String, roleid: String) {
        val request = object : StringRequest(
            Method.POST, url2,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(
                        applicationContext,
                        "Usuário Inserido com Sucesso",
                        Toast.LENGTH_LONG
                    ).show()
                    showDataMhs()
                    mhsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(applicationContext, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
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
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun deleteUser(id: String) {
        val request = object : StringRequest(Method.POST, url3,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Usuário Excluído", Toast.LENGTH_LONG).show()
                    showDataMhs()
                    mhsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(applicationContext, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["user_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onLongItemClick(item: HashMap<String, String>, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Deseja Remover o Usuário?")
        dialog.setPositiveButton("Sim") { _: DialogInterface, _: Int ->
            deleteUser(item["user_id"]!!)
        }
        dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->
            Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG)
                .show()
        }
        dialog.show()
    }

}