package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplan2.R
import com.example.mealplan2.adapters.HistAdapter
import com.example.mealplan2.controller.HistoryController
import com.example.mealplan2.controller.LoginController
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    lateinit var mhsAdapter: HistAdapter
    lateinit var mHistoryController: HistoryController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        mHistoryController = HistoryController()

        floatDelHist.setOnClickListener {
            if (LoginController.currentPrivilege == "Administração") {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Deseja Limpar o Histórico?")
                dialog.setPositiveButton("Sim") { _: DialogInterface, _: Int ->
                    mHistoryController.deleteHist(this, mhsAdapter)
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
        mhsAdapter = HistAdapter(HistoryController.getdata)
        rv_hist.adapter = mhsAdapter

        mHistoryController.showDataMhs(this, mhsAdapter)

        imageBackHist.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        }

    }
}