package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplan2.R
import com.example.mealplan2.adapters.PratosAdapter
import com.example.mealplan2.controller.PratosController
import kotlinx.android.synthetic.main.activity_pratos.*

class PratosActivity : AppCompatActivity(), PratosAdapter.onLongClickListener {

    var spinnerList: ArrayList<String> = ArrayList()

    lateinit var mhsAdapter: PratosAdapter
    lateinit var mPratosController: PratosController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pratos)

        mPratosController = PratosController()

        txCatName.text = "Todos"

        rv_pratos.layoutManager = LinearLayoutManager(this)
        rv_pratos.setHasFixedSize(true)
        mhsAdapter = PratosAdapter(PratosController.getdata, this)
        rv_pratos.adapter = mhsAdapter

        mPratosController.getCategories(this, spinnerPrato, spinnerList)
        mPratosController.showDataMhs(this, mhsAdapter)

        var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinnerList
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerPrato.adapter = adapterSpinner

        imageBackPratos.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        }

        imageCat.setOnClickListener {
            val textSpinner = spinnerPrato.selectedItem.toString()
            if (textSpinner.isNotEmpty()) {
                mPratosController.getPratosByCat(textSpinner, this, mhsAdapter)
                txCatName.text = textSpinner
            }
        }

        floatingAddPrato.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Adicionar Prato")
            dialog.setMessage("Preencha os Dados")
            val view = LayoutInflater.from(this).inflate(R.layout.pratos_dialog, null)
            dialog.setView(view)
            val prato = view.findViewById<EditText>(R.id.edt_nome_prato)
            val desc = view.findViewById<EditText>(R.id.edt_desc_prato)
            val valor = view.findViewById<EditText>(R.id.edt_valor_prato)

            //setando spinner
            var spinner = view.findViewById<Spinner>(R.id.spinnerFiltro)
            var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, spinnerList
            )
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterSpinner


            dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                mPratosController.insertFood(
                    prato.text.toString().trim(),
                    desc.text.toString().trim(),
                    valor.text.toString().trim(),
                    spinner.selectedItem.toString(),
                    this,
                    mhsAdapter
                )
            }
            dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->
                Toast.makeText(this, "Cancelar", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId) {
            R.id.criar_cat -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Deseja Adicionar uma Nova Categoria?")
                val view = LayoutInflater.from(this).inflate(R.layout.category_dialog, null)
                dialog.setView(view)
                val nome = view.findViewById<EditText>(R.id.edt_nome_cat)

                dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                    var cat = nome.text.toString()
                    mPratosController.insertCat(cat, this, mhsAdapter)

                    Handler().postDelayed({
                        mPratosController.getCategories(this, spinnerPrato, spinnerList)
                    }, 2000)

                }
                dialog.setNeutralButton("Cancelar") { _: DialogInterface, i: Int ->
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
                }
                dialog.show()

            }

            R.id.delete_cat ->{
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Deseja Excluir Alguma Categoria?")
                dialog.setMessage("Selecione a Categoria")
                val view = LayoutInflater.from(this).inflate(R.layout.del_cat_dialog, null)
                dialog.setView(view)
                val spinnerDel = view.findViewById<Spinner>(R.id.spinnerDelCat)

                var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, spinnerList
                )
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDel.adapter = adapterSpinner

                dialog.setPositiveButton("Excluir") { _: DialogInterface, _: Int ->
                    var catName = spinnerDel.selectedItem.toString()
                    mPratosController.deleteCat(catName, this)

                    Handler().postDelayed({
                        mPratosController.getCategories(this, spinnerPrato, spinnerList)
                    }, 2000)
                }
                dialog.setNeutralButton("Cancelar") { _: DialogInterface, i: Int ->
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
                }
                dialog.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onLongItemClick(item: HashMap<String, String>, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Prato Selecionado:")
        dialog.setMessage(item["food_name"])
        val view = LayoutInflater.from(this).inflate(R.layout.pratos_dialog, null)
        dialog.setView(view)
        val prato = view.findViewById<EditText>(R.id.edt_nome_prato)
        val desc = view.findViewById<EditText>(R.id.edt_desc_prato)
        val valor = view.findViewById<EditText>(R.id.edt_valor_prato)
        var id = item["food_id"]

        //setando spinner
        var spinnerUpdate = view.findViewById<Spinner>(R.id.spinnerFiltro)
        var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinnerList
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUpdate.adapter = adapterSpinner


        prato.setText(item["food_name"])
        desc.setText(item["food_description"])
        valor.setText(item["food_price"])
        dialog.setPositiveButton("Alterar") { _: DialogInterface, _: Int ->
            if (id != null) {
                mPratosController.updateFood(
                    id, prato.text.toString().trim(),
                    desc.text.toString().trim(),
                    valor.text.toString().trim(),
                    spinnerUpdate.selectedItem.toString(),
                    this,
                    txCatName,
                    mhsAdapter
                )
            }

        }
        dialog.setNegativeButton("Excluir") { _: DialogInterface, i: Int ->
            if (id != null) {
                mPratosController.deleteFood(id, this, mhsAdapter)
            }

        }
        dialog.setNeutralButton("Cancelar") { _: DialogInterface, i: Int ->
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

}