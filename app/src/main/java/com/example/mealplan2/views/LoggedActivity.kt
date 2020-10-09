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

        viewPratos.setOnClickListener {
            Toast.makeText(this, "Gerenciando Pratos", Toast.LENGTH_SHORT).show()
        }

        viewPedidos.setOnClickListener {
            Toast.makeText(this, "Gerenciando Pedidos", Toast.LENGTH_SHORT).show()
        }

        viewHist.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        viewNewLogin.setOnClickListener {
            Toast.makeText(this, "Gerenciando os Usuários", Toast.LENGTH_SHORT).show()
        }
    }
}