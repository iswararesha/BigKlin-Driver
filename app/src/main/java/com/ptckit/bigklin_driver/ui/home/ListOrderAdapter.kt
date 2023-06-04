package com.ptckit.bigklin_driver.ui.home

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ptckit.bigklin_driver.R
import com.ptckit.bigklin_driver.api.base.ApiConfig
import com.ptckit.bigklin_driver.databinding.ItemOrderBinding
import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.model.PaketLaundry
import com.ptckit.bigklin_driver.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListOrderAdapter (private val listData: List<Order>):
    RecyclerView.Adapter<ListOrderAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(order: Order)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemOrderBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            val data = listData[position]

            tvOrderId.text = "Kode Order: " + data.order_id
            tvUserId.text = "Name Pemesan: " + data.user_id
            tvDistance.text = "Harga Paket: " + data.harga_produk.toString() + " /Kg"
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listData[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listData.size
}