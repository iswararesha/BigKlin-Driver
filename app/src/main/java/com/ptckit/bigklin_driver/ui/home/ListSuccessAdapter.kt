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
import com.ptckit.bigklin_driver.databinding.ItemSuccessBinding
import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.model.PaketLaundry
import com.ptckit.bigklin_driver.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ListSuccessAdapter (private val listData: List<Order>):
    RecyclerView.Adapter<ListSuccessAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSuccessBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_success, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            val data = listData[position]

            val date = data.updated_at.let {
                SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(
                    it)
            }

            tvOrderId.text = "Kode Order: " + data.order_id
            tvDate.text = date
            tvUserId.text = "Name Pemesan: " + data.nama_pelanggan
            tvDistance.text = "Harga Total: Rp." + data.harga_total.toString()
        }
    }

    override fun getItemCount(): Int = listData.size
}