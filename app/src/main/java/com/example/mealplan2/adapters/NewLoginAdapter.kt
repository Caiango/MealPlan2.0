package com.example.mealplan2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealplan2.R
import kotlinx.android.synthetic.main.activity_pratos.*
import org.json.JSONObject

class NewLoginAdapter(
    val dataList: List<HashMap<String, String>>,
    var longClickListener: onLongClickListener
) :
    RecyclerView.Adapter<NewLoginAdapter.HolderData>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.all_users_layout, parent, false)
        return HolderData(v)
    }

    override fun onBindViewHolder(holder: HolderData, position: Int) {
        val data = dataList[position]
        holder.txUser.text = data["user_name"]
        holder.txCPF.text = "CPF: " + data["user_cpf"]
        holder.txRole.text = data["role_name"]

        holder.initializeLong(dataList[position], longClickListener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txUser = v.findViewById<TextView>(R.id.txNewUser)
        val txCPF = v.findViewById<TextView>(R.id.txNewCPF)
        val txRole = v.findViewById<TextView>(R.id.txNewRole)

        fun initializeLong(
            item: HashMap<String, String>,
            action: onLongClickListener
        ) {

            itemView.setOnLongClickListener {
                action.onLongItemClick(item, adapterPosition)
                true
            }

        }

    }


    interface onLongClickListener {
        fun onLongItemClick(item: HashMap<String, String>, position: Int) {

        }
    }


}