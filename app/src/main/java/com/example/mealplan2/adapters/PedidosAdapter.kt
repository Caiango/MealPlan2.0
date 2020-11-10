package com.example.mealplan2.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplan2.R

class PedidosAdapter(
    val dataList: List<HashMap<String, String>>,
    var longClickListener: onLongClickListener
) :
    RecyclerView.Adapter<PedidosAdapter.HolderData>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ped_layout, parent, false)
        return HolderData(v)
    }

    override fun onBindViewHolder(holder: HolderData, position: Int) {
        val data = dataList[position]
        holder.txMesa.setText("Mesa " + data["table_number"] + " | " + data["client_name"])
        holder.txDesc.setText(data["order_description"])
        holder.txPrice.setText("R$ " + data["order_price"])
        holder.txPrice.setTextColor(Color.RED)
        holder.txData.setText(data["order_date"])
        holder.txTime.setText(data["order_time"])

        holder.initializeLong(dataList[position], longClickListener)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txMesa = v.findViewById<TextView>(R.id.txMesaPed)
        val txDesc = v.findViewById<TextView>(R.id.txDescPed)
        val txData = v.findViewById<TextView>(R.id.txDataPed)
        val txTime = v.findViewById<TextView>(R.id.txTimePed)
        val txPrice = v.findViewById<TextView>(R.id.txValorPed)

        fun initializeLong(item: HashMap<String, String>, action: onLongClickListener) {

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