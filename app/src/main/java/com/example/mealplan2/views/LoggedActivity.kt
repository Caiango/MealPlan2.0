package com.example.mealplan2.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mealplan2.R
import kotlinx.android.synthetic.main.activity_logged.*

class LoggedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        logged_tolbar.title = LoginActivity.currentUser + " | " + LoginActivity.currentPrivilege

        if (LoginActivity.currentPrivilege == "Atendimento") {
            viewPratos.setOnClickListener {
                Toast.makeText(
                    applicationContext,
                    "Você não tem permissão de acesso!",
                    Toast.LENGTH_LONG
                ).show()
            }

            viewPedidos.setOnClickListener {
                val intent = Intent(this, PedidosActivity::class.java)
                startActivity(intent)
            }

            viewHist.setOnClickListener {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }

            viewNewLogin.setOnClickListener {
                Toast.makeText(
                    applicationContext,
                    "Você não tem permissão de acesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (LoginActivity.currentPrivilege == "Cozinha") {
            viewPratos.setOnClickListener {
                Toast.makeText(
                    applicationContext,
                    "Você não tem permissão de acesso!",
                    Toast.LENGTH_LONG
                ).show()
            }

            viewPedidos.setOnClickListener {
                val intent = Intent(this, PedidosActivity::class.java)
                startActivity(intent)
            }

            viewHist.setOnClickListener {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }

            viewNewLogin.setOnClickListener {
                Toast.makeText(
                    applicationContext,
                    "Você não tem permissão de acesso!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            viewPratos.setOnClickListener {
                val intent = Intent(this, PratosActivity::class.java)
                startActivity(intent)
            }

            viewPedidos.setOnClickListener {
                val intent = Intent(this, PedidosActivity::class.java)
                startActivity(intent)
            }

            viewHist.setOnClickListener {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }

            viewNewLogin.setOnClickListener {
                val intent = Intent(this, NewLoginActivity::class.java)
                startActivity(intent)
            }

        }


        imageBackMan.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            LoginActivity.currentUser = ""
            LoginActivity.currentPrivilege = ""
            Toast.makeText(this, "Você Saiu!", Toast.LENGTH_SHORT).show()
        }
    }
}