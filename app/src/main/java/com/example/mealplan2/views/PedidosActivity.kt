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
import com.example.mealplan2.R
import com.example.mealplan2.adapters.PedidosAdapter
import com.example.mealplan2.controller.LoginController
import com.example.mealplan2.controller.PedidosController
import kotlinx.android.synthetic.main.activity_pedidos.*

class PedidosActivity : AppCompatActivity(), PedidosAdapter.onLongClickListener {

    companion object {
        lateinit var mhsAdapter: PedidosAdapter
    }


    lateinit var mPedidosController: PedidosController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        mPedidosController = PedidosController()

        rv_ped.layoutManager = LinearLayoutManager(this)
        rv_ped.setHasFixedSize(true)
        mhsAdapter = PedidosAdapter(PedidosController.getdata, this)
        rv_ped.adapter = mhsAdapter

        mPedidosController.showDataMhs(this, mhsAdapter)

        imageBackPed.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        }

        floatUpdate.setOnClickListener { mPedidosController.showDataMhs(this, mhsAdapter) }

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
                    mPedidosController.updateOrder(
                        id,
                        desc.text.toString().trim(),
                        this,
                        mhsAdapter
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
                    mPedidosController.deleteOrder(id, this, mhsAdapter)
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
                    mPedidosController.finishOrder(id, this, mhsAdapter)
                }

            }
        }
        dialog.show()

    }


}