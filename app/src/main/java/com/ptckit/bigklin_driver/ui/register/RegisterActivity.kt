package com.ptckit.bigklin_driver.ui.register

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ptckit.bigklin_driver.preference.UserPreference
import com.ptckit.bigklin_driver.ui.ViewModelFactory
import com.ptckit.bigklin_driver.ui.home.HomeActivity
import com.ptckit.bigklin_driver.ui.login.LoginActivity
import com.ptckit.bigklin_driver.databinding.ActivityRegisterBinding
import com.ptckit.bigklin_driver.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupActions()
    }

    private fun setupActions() {
        binding.btnRegister.setOnClickListener {
            actionRegister()
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun actionRegister() {
        val name = binding.edtName
        val email = binding.edtEmail
        val password = binding.edtPassword
        val repassword = binding.edtConfirmPassword
        val address = binding.edtAddress
        val phone = binding.edtPhone

        if(checkValidation(name, email, password, repassword, address, phone)){
            registerViewModel.register(
                name.text.toString().trim(),
                email.text.toString().trim(),
                password.text.toString().trim(),
                address.text.toString().trim(),
                phone.text.toString().trim())
        }
    }

    private fun checkValidation(name: EditText, email: EditText, password: EditText, repassword: EditText, address: EditText, phone: EditText): Boolean {
        val isNameError : Boolean = name.length() == 0
        val isEmailError : Boolean = email.length() == 0
        val isEmailFormatError : Boolean = !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()
        val isPasswordError : Boolean = password.length() < 8 || password.length() > 12
        val isRePasswordError : Boolean = repassword.length() == 0
        val isPasswordMatch : Boolean = password.text.toString() === repassword.text.toString()
        val isAddressError : Boolean = address.length() == 0
        val isPhoneError : Boolean = phone.length() < 11 || phone.length() > 13
        val isError : Boolean = isNameError|| isEmailError ||
                isEmailFormatError || isPasswordError ||
                isRePasswordError || isPasswordMatch ||
                isAddressError || isPhoneError

        Log.d("password", password.text.toString() + "-" + repassword.text.toString())
        if(isError){
            if(isNameError){
                name.error = "Nama harus diisi!"
            }
            if(isEmailError){
                email.error = "Email harus diisi!"
            }
            if(isEmailFormatError){
                email.error = "Isi Email dengan benar"
            }
            if(isPasswordError){
                password.error = "Password harus diisi!"
            }
            if(isRePasswordError){
                repassword.error = "Konfirmasi Password harus diisi!"
            }
            if(isPasswordMatch){
                repassword.error = "Konfirmasi Password tidak sesuai"
            }
            if(isAddressError){
                address.error = "Alamat harus diisi!"
            }
            if(isPhoneError){
                phone.error = "Nomor HP tidak sesuai!"
            }

            return false
        }

        return true
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.message.observe(this) { message ->
            Toast.makeText(this@RegisterActivity, message.toString(), Toast.LENGTH_SHORT).show()
        }

        registerViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
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

    private fun showLoading(isLoading: Boolean) {
        if(isLoading){
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.loading.visibility = View.GONE
        }
    }
}