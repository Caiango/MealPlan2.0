package com.example.mealplan2.views

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mealplan2.R
import kotlinx.android.synthetic.main.activity_pratos.*

class PratosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pratos)

        floatingAddPrato.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Prato Selecionado")
            dialog.setMessage("Prato X")
            val view = LayoutInflater.from(this).inflate(R.layout.pratos_dialog, null)
            dialog.setView(view)
            val prato = view.findViewById<EditText>(R.id.edt_nome_prato)
            val desc = view.findViewById<EditText>(R.id.edt_desc_prato)
            val valor = view.findViewById<EditText>(R.id.edt_valor_prato)

            dialog.setPositiveButton("Alterar") { _: DialogInterface, _: Int ->
                Toast.makeText(applicationContext, "Prato Alterado", Toast.LENGTH_LONG).show()
            }
            dialog.setNegativeButton("Excluir") { _: DialogInterface, i: Int ->
                Toast.makeText(this, "Prato Excluído", Toast.LENGTH_SHORT).show()
            }
            dialog.setNeutralButton("Cancelar") { _: DialogInterface, i: Int ->
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
    }
}