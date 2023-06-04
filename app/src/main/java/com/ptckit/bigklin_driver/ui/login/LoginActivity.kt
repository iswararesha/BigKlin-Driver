package com.ptckit.bigklin_driver.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.ptckit.bigklin_driver.ui.register.RegisterActivity
import com.ptckit.bigklin_driver.databinding.ActivityLoginBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupActions()
    }

    private fun setupActions() {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            actionLogin()
        }
    }

    private fun actionLogin() {
        val email = binding.edtEmail
        val password = binding.edtPassword

        if(checkValidation(email, password)){
            loginViewModel.login(email.text.toString().trim(), password.text.toString().trim())
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.message.observe(this) { message ->
            Toast.makeText(this@LoginActivity, message.toString(), Toast.LENGTH_SHORT).show()
        }

        loginViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
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

    private fun checkValidation(email: EditText, password: EditText) : Boolean {
        val isEmailError : Boolean = email.length() == 0
        val isEmailFormatError : Boolean = !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()
        val isPasswordError : Boolean = password.length() == 0
        val isError : Boolean = isEmailError || isEmailFormatError || isPasswordError

        if(isError){
            if(isEmailError){
                email.error = "Email harus diisi!"
            }
            if(isEmailFormatError){
                email.error = "Isi Email dengan benar"
            }
            if(isPasswordError){
                password.error = "Password harus diisi!"
            }

            return false
        }

        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading){
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.loading.visibility = View.GONE
        }
    }
}