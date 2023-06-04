package com.ptckit.bigklin_driver.ui.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.ptckit.bigklin_driver.databinding.ActivityPaymentBinding
import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.preference.UserPreference
import com.ptckit.bigklin_driver.ui.ViewModelFactory
import com.ptckit.bigklin_driver.ui.detail.QRCodeActivity.Companion.IMAGE
import com.ptckit.bigklin_driver.ui.home.HomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var order : Order

    private var token = "User Token"

    companion object{
        const val PAKET_DETAIL = "data_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        order = intent.getParcelableExtra<Order>(PAKET_DETAIL) as Order

        setupView()
        setupViewModel()
        setupAction()

        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            binding.popupWindowBackground.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()


        // Fade animation for the Popup Window
        binding.popupWindowViewWithBorder.alpha = 0f
        binding.popupWindowViewWithBorder.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()
    }

    private fun setupAction(){
        binding.btnCash.setOnClickListener {
            val berat = intent.getIntExtra("berat", 0)

            if(token == "User Token"){

            } else {
                detailViewModel.setPaymentCash(token, order.order_id, berat)
                Toast.makeText(this@PaymentActivity, "Pembayaran berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.btnQR.setOnClickListener {
            val berat = intent.getIntExtra("berat", 0)

            if(token == "User Token"){

            } else {
                detailViewModel.setPaymentDigital(token, order.order_id, berat)
            }
        }
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

        getToken()

        val berat = intent.getIntExtra("berat", 0)
        val total = (berat * order.harga_produk) + order.harga_antar
        binding.tvWeight.text = "Berat: $berat"
        binding.tvTotal.text = "Total: $total"


        detailViewModel.QRLink.observe(this){
            val intent = Intent(this, QRCodeActivity::class.java)
            intent.putExtra(IMAGE, it)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

            detailViewModel.QRLink.removeObservers(this)
        }
    }

    private fun getToken(): String {
        detailViewModel.getToken().observe(this) { it ->
            if (it != "") {
                token = it
            } else {
                Toast.makeText(this@PaymentActivity, "Login Not Valid", Toast.LENGTH_SHORT).show()
            }
        }

        return token
    }

    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            binding.popupWindowBackground.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        binding.popupWindowViewWithBorder.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }
}