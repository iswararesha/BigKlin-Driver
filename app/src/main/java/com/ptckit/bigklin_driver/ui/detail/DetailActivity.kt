package com.ptckit.bigklin_driver.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ptckit.bigklin_driver.R
import com.ptckit.bigklin_driver.databinding.ActivityDetailBinding
import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.preference.UserPreference
import com.ptckit.bigklin_driver.ui.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var order : Order
    private lateinit var mMap: GoogleMap


    private var token = "User Token"

    companion object{
        const val PAKET_DETAIL = "data_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        order = intent.getParcelableExtra<Order>(PAKET_DETAIL) as Order

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
        setupView()
        setupAction()
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
        detailViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailViewModel::class.java]

        binding.tvName.text = "Nama: " + order.nama_pelanggan
        binding.tvAdress.text = "Alamat: " + order.alamat_pelanggan
        binding.tvPhone.text = "Nomor HP: " + order.nomor_hp_pelanggan
    }

    private fun setupAction(){
        binding.btnPayment.setOnClickListener{
            val berat = binding.edtWeight.text.toString().toInt()

            if(binding.edtWeight.length() == 0){
                binding.edtWeight.error = "Berat harus diisi!"
            } else {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra(PaymentActivity.PAKET_DETAIL, order)
                intent.putExtra("berat", berat)
                startActivity(intent)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val address = LatLng(order.latitude, order.longitude)
        Log.d("Map", order.latitude.toString() + " " + order.longitude.toString())
        mMap.addMarker(MarkerOptions()
            .position(address)
            .title("Alamat Tujuan"))
        val zoomLevel = 16.0f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, zoomLevel))
        mMap.uiSettings.setAllGesturesEnabled(false)

        mMap.setOnMapClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=${order.latitude},${order.longitude} (" + order.nama_pelanggan + ")"))
            startActivity(intent)
        }
    }
}