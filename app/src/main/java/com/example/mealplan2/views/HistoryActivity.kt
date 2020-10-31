package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.R
import com.example.mealplan2.adapters.HistAdapter
import kotlinx.android.synthetic.main.activity_history.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class HistoryActivity : AppCompatActivity() {

    var getdata = mutableListOf<HashMap<String, String>>()
//    var url = "http://192.168.0.22/php_android/show_history.php"
//    var url2 = "http://192.168.0.22/php_android/delete_all_hist.php"

    var url = "http://192.168.1.2/meal_plan2/show_history.php"
    var url2 = "http://192.168.1.2/meal_plan2/delete_all_hist.php"

    lateinit var mhsAdapter: HistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        floatDelHist.setOnClickListener {
            if (LoginActivity.currentPrivilege == "Administração") {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Deseja Limpar o Histórico?")
                dialog.setPositiveButton("Sim") { _: DialogInterface, _: Int ->
                    deleteHist()
                }
                dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->

                    Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG).show()
                }
                dialog.show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Você não tem permissão de acesso!",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        rv_hist.layoutManager = LinearLayoutManager(this)
        rv_hist.setHasFixedSize(true)
        mhsAdapter = HistAdapter(getdata)
        rv_hist.adapter = mhsAdapter
        showDataMhs()

        imageBackHist.setOnClickListener {
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
                        getdata.add(mhs)
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Não há histórico", Toast.LENGTH_LONG).show()
                }
                mhsAdapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun deleteHist() {
        val request = object : StringRequest(Method.POST, url2,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Histórico Excluído", Toast.LENGTH_LONG)
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
//            override fun getParams(): MutableMap<String, String> {
//                val hm = HashMap<String, String>()
//
//                return hm
//            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}