package com.example.mealplan2.views

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mealplan2.R
import kotlinx.android.synthetic.main.activity_new_login.*

class NewLoginActivity : AppCompatActivity() {

    var privilegesList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_login)

        privilegesList.add("Administração")
        privilegesList.add("Cozinha")
        privilegesList.add("Atendimento")

        var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, privilegesList
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newSpinner.adapter = adapterSpinner

        imageBackNew.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        }


    }

}