package com.example.moisoftqr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class QRAdapter(
    var values: ArrayList<HashMap<String, String>>,
    val onclick: OnItemClickListener?
) :
    RecyclerView.Adapter<QRAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_qr, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = values.get(position)

        holder.txt_str?.text = model.get("QRvalue")




        holder.itemView.setOnClickListener {
            onclick?.onItemClick(model)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_str = view.findViewById<TextView>(R.id.txt_str)
    }

    fun updateList(list: ArrayList<HashMap<String, String>>) {
        values = list
        notifyDataSetChanged()
    }


}
