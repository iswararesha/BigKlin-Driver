package com.ptckit.bigklin_driver.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ptckit.bigklin_driver.R
import com.ptckit.bigklin_driver.preference.UserPreference
import com.ptckit.bigklin_driver.databinding.ActivityHomeBinding
import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.ui.ViewModelFactory
import com.ptckit.bigklin_driver.ui.detail.DetailActivity
import com.ptckit.bigklin_driver.ui.login.LoginActivity
import com.ptckit.bigklin_driver.ui.main.MainActivity
import com.ptckit.bigklin_driver.ui.map.MapActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private var token = "User Token"

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getListOrder(): String {
        homeViewModel.getToken().observe(this) { it ->
            if (it != "") {
                token = it
                homeViewModel.getListOrder(it)
            } else {
                Toast.makeText(this@HomeActivity, "Login Not Valid", Toast.LENGTH_SHORT).show()
            }
        }

        return token
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]

        getListOrder()

        homeViewModel.isLoading.observe(this){
            showLoading(it)
        }

        homeViewModel.orderData.observe(this){
            setListOrder(it)
        }
    }
    private fun setListOrder(listOrder: List<Order>?) {
        binding.rvListOrder.layoutManager = LinearLayoutManager(this)

        val listOrderAdapter = listOrder?.let { ListOrderAdapter(it) }
        binding.rvListOrder.adapter = listOrderAdapter

        listOrderAdapter?.setOnItemClickCallback(object: ListOrderAdapter.OnItemClickCallback{
            override fun onItemClicked(order: Order){
                showSelectedOrder(order)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading){
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.loading.visibility = View.GONE
        }
    }

    private fun showSelectedOrder(order: Order) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.PAKET_DETAIL, order)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }
}