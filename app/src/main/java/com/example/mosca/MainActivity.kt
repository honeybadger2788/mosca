package com.example.mosca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
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
                startActivity(Intent(this, RegisterActivity::class.java))
            } else {
                showMessage("Algo salió mal")
            }
        }

        binding.cbRememberme.setOnCheckedChangeListener { _, _ ->
            if(binding.cbRememberme.isChecked){
                showMessage("No me olvides!")
            } else {
                showMessage("Olvidate de mi!")
            }
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.llLogin)
            .show()
    }
}