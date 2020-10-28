package com.example.mealplan2.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mealplan2.R
import kotlinx.android.synthetic.main.activity_logged.*

class LoggedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        Toast.makeText(applicationContext, "Olá!" + " " + LoginActivity.currentUser + " / " + LoginActivity.currentPrivilege, Toast.LENGTH_LONG)
            .show()

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

        imageBackMan.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            LoginActivity.currentUser = ""
            LoginActivity.currentPrivilege = ""
            Toast.makeText(this, "Você Saiu!", Toast.LENGTH_SHORT).show()
        }
    }
}