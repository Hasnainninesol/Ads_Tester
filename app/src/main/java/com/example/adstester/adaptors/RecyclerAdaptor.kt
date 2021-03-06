package com.example.adstester.adaptors

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adstester.R
import com.example.adstester.ads.loadNativeAd
import com.example.adstester.databinding.LargeNativeBinding
import com.example.adstester.databinding.RecycleritemBinding
import com.google.android.gms.ads.nativead.NativeAd

class RecyclerAdaptor(private var list: ArrayList<Any>, private var context: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binder = RecycleritemBinding.inflate(LayoutInflater.from(context), parent, false)
            MyViewHolder(binder)
        } else {
            val binder = LargeNativeBinding.inflate(LayoutInflater.from(context), parent, false)
            AddViewHolder(binder)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            with(holder as MyViewHolder) {
                binding.tvTitle.text = list[position].toString()
            }
        } else {
            with(holder as AddViewHolder) {
                val nv = list[position] as NativeAd
                context.loadNativeAd(
                    binding.flAdplaceholder,
                    R.layout.native_layout,
                    binding.shimmer, nv
                )

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem: Any = list[position]
        if (recyclerViewItem is NativeAd) {
            return 0;
        }
        return 1
    }

    class MyViewHolder(val binding: RecycleritemBinding) : RecyclerView.ViewHolder(binding.root)
    class AddViewHolder(val binding: LargeNativeBinding) : RecyclerView.ViewHolder(binding.root)


}