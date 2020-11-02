package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplan2.R
import com.example.mealplan2.adapters.NewLoginAdapter
import com.example.mealplan2.controller.NewLoginController
import kotlinx.android.synthetic.main.activity_new_login.*

class NewLoginActivity : AppCompatActivity(), NewLoginAdapter.onLongClickListener {

    lateinit var mhsAdapter: NewLoginAdapter
    lateinit var mNewLoginController: NewLoginController

    companion object {
        var roleAct = NewLoginController.role
    }

    var privilegesList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_login)

        mNewLoginController = NewLoginController()

        imageBackNew.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        }

        rvNewUsers.layoutManager = LinearLayoutManager(this)
        rvNewUsers.setHasFixedSize(true)
        mhsAdapter = NewLoginAdapter(NewLoginController.getdata, this)
        rvNewUsers.adapter = mhsAdapter

        mNewLoginController.showDataMhs(this, mhsAdapter)
        mNewLoginController.getRoles(this, mhsAdapter, newSpinner, privilegesList)

        newUser.setOnClickListener {
            var nome = edtNewUser.text.toString().trim()
            var senha = edtNewPass.text.toString().trim()
            var cpf = edtCPF.text.toString().trim()

            val textSpinner = newSpinner.selectedItem.toString()

            mNewLoginController.selectedRole(textSpinner, this)

            Handler().postDelayed({
                if (roleAct.isNotBlank()) {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Confirma os Dados?")
                    dialog.setMessage("Nome: $nome \nCPF: $cpf\nSenha: $senha")
                    dialog.setPositiveButton("Sim") { _: DialogInterface, _: Int ->
                        mNewLoginController.insertUser(nome, cpf, senha, roleAct, this, mhsAdapter)
                    }
                    dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->
                        Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG)
                            .show()
                    }
                    dialog.show()
                }
            }, 1000)

            if (roleAct.isNotBlank()) {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Confirma os Dados?")
                dialog.setMessage("Nome: $nome \nCPF: $cpf\nSenha: $senha")
                dialog.setPositiveButton("Sim") { _: DialogInterface, _: Int ->
                    mNewLoginController.insertUser(nome, cpf, senha, roleAct, this, mhsAdapter)
                }
                dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->
                    Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG)
                        .show()
                }
                dialog.show()
            }
        }

    }

    override fun onLongItemClick(item: HashMap<String, String>, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Deseja Remover o Usuário?")
        dialog.setPositiveButton("Sim") { _: DialogInterface, _: Int ->
            mNewLoginController.deleteUser(item["user_id"]!!, this, mhsAdapter)
        }
        dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->
            Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG)
                .show()
        }
        dialog.show()
    }


}