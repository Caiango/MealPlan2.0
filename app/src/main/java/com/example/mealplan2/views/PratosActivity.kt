package com.example.mealplan2.views

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.R
import com.example.mealplan2.adapters.HistAdapter
import com.example.mealplan2.adapters.PratosAdapter
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_pratos.*
import kotlinx.android.synthetic.main.pratos_dialog.*
import org.json.JSONArray
import org.json.JSONObject

class PratosActivity : AppCompatActivity(), PratosAdapter.onLongClickListener {
    var getdata = mutableListOf<HashMap<String, String>>()
    var url = "http://192.168.1.2/meal_plan2/show_pratos.php"
    var url2 = "http://192.168.1.2/meal_plan2/get_categories.php"
    var url3 = "http://192.168.1.2/meal_plan2/insert_prato.php"
    var url4 = "http://192.168.1.2/meal_plan2/delete_prato.php"
    var url5 = "http://192.168.1.2/meal_plan2/update_prato.php"
    var url6 = "http://192.168.1.2/meal_plan2/select_by_category.php"

    var spinnerList: ArrayList<String> = ArrayList()

    lateinit var mhsAdapter: PratosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pratos)

        rv_pratos.layoutManager = LinearLayoutManager(this)
        rv_pratos.setHasFixedSize(true)
        mhsAdapter = PratosAdapter(getdata, this)
        rv_pratos.adapter = mhsAdapter

        getCategories()
        showDataMhs()


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
                getPratosByCat(textSpinner)
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
            val cat = view.findViewById<Spinner>(R.id.spinnerFiltro)

            //setando spinner
            var spinner = view.findViewById<Spinner>(R.id.spinnerFiltro)
            var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, spinnerList
            )
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterSpinner


            dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
//                Toast.makeText(applicationContext, "Prato Adicionado", Toast.LENGTH_LONG).show()
                insertFood(
                    prato.text.toString().trim(),
                    desc.text.toString().trim(),
                    valor.text.toString().trim(),
                    cat.selectedItem.toString()

                )
            }
            dialog.setNegativeButton("Cancelar") { _: DialogInterface, i: Int ->
                Toast.makeText(this, "Cancelar", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
    }

    fun showDataMhs() {
        val request = StringRequest(
            Request.Method.POST, url,
            { response ->
                getdata.clear()
                try {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var mhs = HashMap<String, String>()
                        mhs.put("food_name", jsonObject.getString("food_name"))
                        mhs.put("food_description", jsonObject.getString("food_description"))
                        mhs.put("food_price", jsonObject.getString("food_price"))
                        mhs.put("foods_id", jsonObject.getString("foods_id"))
                        getdata.add(mhs)
                        mhsAdapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Não há Pratos Cadastrados",
                        Toast.LENGTH_LONG
                    ).show()
                }
                mhsAdapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
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
        var id = item["foods_id"]

        //setando spinner
        var spinner = view.findViewById<Spinner>(R.id.spinnerFiltro)
        var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinnerList
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        prato.setText(item["food_name"])
        desc.setText(item["food_description"])
        valor.setText(item["food_price"])
        dialog.setPositiveButton("Alterar") { _: DialogInterface, _: Int ->
            if (id != null) {
                updateFood(
                    id, prato.text.toString().trim(),
                    desc.text.toString().trim(),
                    valor.text.toString().trim()
                )
            }

        }
        dialog.setNegativeButton("Excluir") { _: DialogInterface, i: Int ->
            if (id != null) {
                deleteFood(id)
            }

        }
        dialog.setNeutralButton("Cancelar") { _: DialogInterface, i: Int ->
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    fun getCategories() {
        spinnerList.clear()
        val request = StringRequest(
            Request.Method.POST, url2,
            { response ->
                getdata.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(x)

                    //adicionando as categorias obtidas no banco dentro de uma lista
                    spinnerList.add(jsonObject.getString("category_name"))


                }
                //criando variável adapter que vai receber os dados da lista
                var adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, spinnerList
                )
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                //setando adapter criado no meu spinner
                spinnerPrato.adapter = adapterSpinner

            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun insertFood(nome: String, desc: String, valor: String, cat: String) {
        val request = object : StringRequest(Method.POST, url3,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Prato Inserido", Toast.LENGTH_LONG).show()
                    showDataMhs()
                    mhsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(applicationContext, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                val textSpinner = spinnerPrato.selectedItem.toString()
                if (cat == "Prato Principal") {
                    hm.put("category_id", "1")
                } else if (cat == "Petiscos") {
                    hm.put("category_id", "2")
                } else if (cat == "Bebidas") {
                    hm.put("category_id", "3")
                }

                //recebendo e enviando valores para o php
                hm.put("food_name", nome)
                hm.put("food_description", desc)
                hm.put("food_price", valor)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun deleteFood(id: String) {
        val request = object : StringRequest(Method.POST, url4,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Prato Excluído", Toast.LENGTH_LONG).show()
                    showDataMhs()
                    mhsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(applicationContext, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["foods_id"] = id
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun updateFood(id: String, nome: String, desc: String, valor: String) {
        val request = object : StringRequest(Method.POST, url5,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("kode")
                if (error.equals("000")) {
                    Toast.makeText(applicationContext, "Prato Alterado", Toast.LENGTH_LONG).show()
                    showDataMhs()
                    mhsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(applicationContext, "Algo deu errado", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["foods_id"] = id
                hm.put("food_name", nome)
                hm.put("food_description", desc)
                hm.put("food_price", valor)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun getPratosByCat(cat: String) {
        val request = object : StringRequest(Method.POST, url6,
            Response.Listener { response ->
                getdata.clear()

                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    var mhs = HashMap<String, String>()
                    mhs.put("food_name", jsonObject.getString("food_name"))
                    mhs.put("food_description", jsonObject.getString("food_description"))
                    mhs.put("food_price", jsonObject.getString("food_price"))
                    mhs.put("foods_id", jsonObject.getString("foods_id"))
                    getdata.add(mhs)
                }
                mhsAdapter.notifyDataSetChanged()

            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                //recebendo e enviando valores para o php
                hm["category_name"] = cat
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}