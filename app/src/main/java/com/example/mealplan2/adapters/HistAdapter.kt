package com.example.mealplan2.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplan2.R

class HistAdapter(val dataList: List<HashMap<String, String>>) :
    RecyclerView.Adapter<HistAdapter.HolderData>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hist_layout, parent, false)
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

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txMesa = v.findViewById<TextView>(R.id.txMesaHist)
        val txDesc = v.findViewById<TextView>(R.id.txDescHist)
        val txData = v.findViewById<TextView>(R.id.txDataHist)
        val txTime = v.findViewById<TextView>(R.id.txTimeHist)
        val txPrice = v.findViewById<TextView>(R.id.txValorHist)

    }

}