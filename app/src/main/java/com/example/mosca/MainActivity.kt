package com.example.mosca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mosca.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            if(binding.etUsername.text.toString().isNotBlank() && binding.etPassword.text.toString().isNotBlank()){
                showMessage("Hola, ${binding.etUsername.text.toString()}")
            } else {
                showMessage("Algo salió mal")
            }
        }

        binding.btnRegister.setOnClickListener{
            showMessage("Hora de registrarse")
        }
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.llLogin)
            .show()
    }
}