package com.ptckit.bigklin_driver.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.ptckit.bigklin_driver.databinding.ActivityQrcodeBinding

class QRCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrcodeBinding
    private lateinit var image: String

    companion object{
        const val IMAGE = "image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        image = intent.getStringExtra(IMAGE) as String

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

        Glide.with(this)
            .load(image)
            .into(binding.imgDetail)
    }

    private fun setupAction(){
        binding.btnClear.setOnClickListener(){
            finish()
        }
    }
}