package com.example.adstester.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adstester.databinding.RecycleritemBinding

class RecyclerAdaptor(private var list: List<String>, private var context: Context) :
    RecyclerView.Adapter<RecyclerAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binder = RecycleritemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binder)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder)
        {
            binding.tvTitle.text = list[position]
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: RecycleritemBinding) : RecyclerView.ViewHolder(binding.root)
}