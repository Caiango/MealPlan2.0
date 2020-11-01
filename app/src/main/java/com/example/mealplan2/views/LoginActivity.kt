package com.example.mealplan2.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mealplan2.R
import com.example.mealplan2.controller.LoginController
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mLoginController: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mLoginController = LoginController()

        btnLogin.setOnClickListener {
            if (cpf.text.isEmpty() || password.text.isEmpty()) {
                cpf.error = "Preencha os valores"
                password.error = "Preencha os valores"
            } else {
                val cpf = cpf.text.toString().trim()
                val pass = password.text.toString().trim()

                mLoginController.getData(cpf, pass, this)


            }
        }

    }
}