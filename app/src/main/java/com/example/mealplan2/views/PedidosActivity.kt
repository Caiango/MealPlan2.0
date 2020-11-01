package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import com.example.mealplan2.adapters.PedidosAdapter
import com.example.mealplan2.controller.LoginController
import kotlinx.android.synthetic.main.activity_pedidos.*
import org.json.JSONArray
import org.json.JSONObject

class PedidosActivity : AppCompatActivity(), PedidosAdapter.onLongClickListener {

    var getdata = mutableListOf<HashMap<String, String>>()
//    var url = "http://192.168.0.22/php_android/show_orders.php"
//    var url2 = "http://192.168.0.22/php_android/delete_order.php"
//    var url3 = "http://192.168.0.22/php_android/update_order.php"
//    var url4 = "http://192.168.0.22/php_android/finish_order.php"

    var url = "http://192.168.1.2/meal_plan2/show_orders.php"
    var url2 = "http://192.168.1.2/meal_plan2/delete_order.php"
    var url3 = "http://192.168.1.2/meal_plan2/update_order.php"
    var url4 = "http://192.168.1.2/meal_plan2/finish_order.php"


    lateinit var mhsAdapter: PedidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        rv_ped.layoutManager = LinearLayoutManager(this)
        rv_ped.setHasFixedSize(true)
        mhsAdapter = PedidosAdapter(getdata, this)
        rv_ped.adapter = mhsAdapter
        showDataMhs()

        imageBackPed.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
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
                        applicationContext,
                        "Não há pedidos em Andamento",
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

    override fun onLongItemClick(item: HashMap<String, String>, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Pedido Selecionado:")
        dialog.setMessage(item["order_description"])
        val view = LayoutInflater.from(this).inflate(R.layout.pedido_dialog, null)
        dialog.setView(view)
        val desc = view.findViewById<EditText>(R.id.edt_desc_pedido)
        var id = item["order_id"]
        desc.setText(item["order_description"])
        dialog.setPositiveButton("Alterar") { _: DialogInterface, _: Int ->
            if (id != null) {
                if (LoginController.currentPrivilege == "Cozinha") {
                    Toast.makeText(
                        applicationContext,
                        "Você não tem permissão de acesso!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    updateOrder(
                        id,
                        desc.text.toString().trim()
                    )
                }

            }
        }
        dialog.setNegativeButton("Excluir") { _: DialogInterface, i: Int ->
            if (id != null) {
                if (LoginController.currentPrivilege == "Atendimento" || LoginController.currentPrivilege == "Cozinha") {
                    Toast.makeText(
                        applicationContext,
                        "Você não tem permissão de acesso!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    deleteOrder(id)
                }

            }
        }
        dialog.setNeutralButton("Finalizar") { _: DialogInterface, i: Int ->
            if (id != null) {
                if (LoginController.currentPrivilege == "Atendimento") {
                    Toast.makeText(
                        applicationContext,
                        "Você não tem permissão de acesso!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    finishOrder(id)
                }

            }
        }
        dialog.show()

    }

    fun deleteOrder(id: String) {
        val request = object : StringRequest(Method.POST, url2,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Pedido Excluído", Toast.LENGTH_LONG).show()
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
                hm["order_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun updateOrder(id: String, desc: String) {
        val request = object : StringRequest(Method.POST, url3,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Pedido Alterado", Toast.LENGTH_LONG).show()
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
                hm["order_id"] = id
                hm.put("order_description", desc)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun finishOrder(id: String) {
        val request = object : StringRequest(Method.POST, url4,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Pedido Finalizado", Toast.LENGTH_LONG)
                        .show()
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
                hm["order_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


}