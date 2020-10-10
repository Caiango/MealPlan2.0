package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.R
import com.example.mealplan2.adapters.HistAdapter
import com.example.mealplan2.adapters.PedidosAdapter
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_pedidos.*
import org.json.JSONArray

class PedidosActivity : AppCompatActivity(), PedidosAdapter.onLongClickListener {

    var getdata = mutableListOf<HashMap<String, String>>()
    var url = "http://192.168.1.2/meal_plan2/show_orders.php"
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

    override fun onLongItemClick(item: HashMap<String, String>, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Pedido Selecionado:")
        dialog.setMessage(item["order_description"])
        val view = LayoutInflater.from(this).inflate(R.layout.pedido_dialog, null)
        dialog.setView(view)
        val desc = view.findViewById<EditText>(R.id.edt_desc_pedido)
        desc.setText(item["order_description"])
        dialog.setPositiveButton("Alterar") { _: DialogInterface, _: Int ->
            Toast.makeText(applicationContext, "Pedido Alterado", Toast.LENGTH_LONG).show()
        }
        dialog.setNegativeButton("Excluir") { _: DialogInterface, i: Int ->
            Toast.makeText(this, "Pedido Excluído", Toast.LENGTH_SHORT).show()
        }
        dialog.setNeutralButton("Finalizar") { _: DialogInterface, i: Int ->
            Toast.makeText(this, "Pedido Finalizado", Toast.LENGTH_SHORT).show()
        }
        dialog.show()

    }


}