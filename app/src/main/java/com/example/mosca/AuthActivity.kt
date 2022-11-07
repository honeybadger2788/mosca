package com.example.mosca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mosca.activity.HomeActivity
import com.example.mosca.activity.RegisterActivity
import com.example.mosca.databinding.ActivityAuthBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        loginUser()

        binding.cbRememberme.setOnCheckedChangeListener { _, _ ->
            if(binding.cbRememberme.isChecked){
                showMessage("No me olvides!")
            } else {
                showMessage("Olvidate de mi!")
            }
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this@AuthActivity, RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        with(binding){
            btnLogin.setOnClickListener{
                if(etEmail.text!!.isNotBlank() && etPassword.text!!.isNotBlank()){
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener{ task ->
                            if(task.isSuccessful)
                                startActivity(Intent(this@AuthActivity, HomeActivity::class.java))
                            else
                                showMessage("Credenciales incorrectas")
                        }
                } else {
                    showMessage("Algo salió mal")
                }
            }
        }
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.llLogin)
            .show()
    }
}