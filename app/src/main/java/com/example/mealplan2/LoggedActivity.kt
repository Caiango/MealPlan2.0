package com.example.mealplan2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_logged.*

class LoggedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        viewPratos.setOnClickListener {
            Toast.makeText(this, "Gerenciando Pratos", Toast.LENGTH_SHORT).show()
        }

        viewPedidos.setOnClickListener {
            Toast.makeText(this, "Gerenciando Pedidos", Toast.LENGTH_SHORT).show()
        }

        viewHist.setOnClickListener {
            Toast.makeText(this, "Gerenciando Histórico", Toast.LENGTH_SHORT).show()
        }

        viewNewLogin.setOnClickListener {
            Toast.makeText(this, "Gerenciando os Usuários", Toast.LENGTH_SHORT).show()
        }
    }
}