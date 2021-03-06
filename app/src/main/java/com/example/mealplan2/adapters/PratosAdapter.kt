package com.example.mealplan2.adapters

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplan2.R
import com.squareup.picasso.Picasso

class PratosAdapter(
    val dataList: List<HashMap<String, String>>,
    var longClickListener: onLongClickListener
) :
    RecyclerView.Adapter<PratosAdapter.HolderData>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.pratos_layout, parent, false)
        return HolderData(v)
    }

    override fun onBindViewHolder(holder: HolderData, position: Int) {
        val data = dataList[position]
        holder.txPrato.setText(data["food_name"])
        holder.txDesc.setText(data["food_description"])
        holder.txValor.setText("R$ " + data["food_price"])
        if (!data["url"].equals("")) {
            Picasso.get().load(data["url"]).into(holder.img)
        }


        holder.initializeLong(dataList[position], longClickListener)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txPrato = v.findViewById<TextView>(R.id.txPrato)
        val txDesc = v.findViewById<TextView>(R.id.txDescPrato)
        val txValor = v.findViewById<TextView>(R.id.txValorPrato)
        val img = v.findViewById<ImageView>(R.id.imgPrato)

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