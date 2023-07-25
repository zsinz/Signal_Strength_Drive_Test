package com.example.ssdt.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ssdt.R
import com.example.ssdt.data.DataSum
import kotlin.math.round



class AdapterSum (
    private val context: Context,
    private val dataset: List<DataSum>,
    private val clickListener: (List<DataSum>)->Unit
    ):RecyclerView.Adapter<AdapterSum.SumViewHolder>(){
    var keadaan = SparseBooleanArray()
    var sumdat = mutableListOf<DataSum>()
    inner class SumViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txt1: TextView = view.findViewById(R.id.txt3a1)
        val txt2: TextView = view.findViewById(R.id.txt3a2)
        val txt3: TextView = view.findViewById(R.id.txt3a3)
        val txt4: TextView = view.findViewById(R.id.txt3a4)
        val chkbx:CheckBox = view.findViewById(R.id.checkBox)
        fun bind(item: DataSum) {
//            chkbx.text = item.dsid.toString()
            txt1.text = "Nama :" + item.nama
            txt2.text = "Rata :" + round(item.datAvg*1000)/1000 + " dBm"
            txt3.text = "Max  :" + item.datMax + " dBm"
            txt4.text = "Min  :" + item.datMin + " dBm"
        }
        init {
            chkbx.setOnClickListener {
                if (!keadaan.get(adapterPosition,false)){
                    chkbx.isChecked =true
                    sumdat.add(dataset[adapterPosition])
                    keadaan.put(adapterPosition,true)
                } else {
                    chkbx.isChecked =false
                    sumdat.remove(dataset[adapterPosition])
                    keadaan.put(adapterPosition,false)
                }
                clickListener(sumdat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SumViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.rec_sum, parent, false)
        Log.d("Adapter","onCreateViewHolder")
        return SumViewHolder(adapterLayout)
    }
    override fun onBindViewHolder(holder: SumViewHolder, position: Int) {
        Log.d("Adapter","onBindViewHolder" + position)
        val item = dataset[position]
        holder.bind(item)
        if (!keadaan.get(position,false)){
            holder.chkbx.isChecked = false
        } else{
            holder.chkbx.isChecked = true
        }
    }
    override fun getItemCount() = dataset.size
}