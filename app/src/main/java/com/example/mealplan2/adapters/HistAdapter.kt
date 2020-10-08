package com.example.mealplan2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplan2.R

class HistAdapter() : RecyclerView.Adapter<HistAdapter.HolderData>() {

    var dataList: List<HashMap<String, String>> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hist_layout, parent, false)
        return HolderData(v)
    }

    override fun onBindViewHolder(holder: HolderData, position: Int) {
        val data = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txMesa = v.findViewById<TextView>(R.id.txMesaPedido)
        val txDesc = v.findViewById<TextView>(R.id.txDescPedido)
        val txData = v.findViewById<TextView>(R.id.txDataPedido)
        val txTime = v.findViewById<TextView>(R.id.txTimePedido)

    }
}