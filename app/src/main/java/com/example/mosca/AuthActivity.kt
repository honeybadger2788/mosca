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

        with(binding){
            btnRegister.setOnClickListener{
                startActivity(Intent(this@AuthActivity, RegisterActivity::class.java))
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // error al presionar el back button del celular
            startActivity(Intent(this@AuthActivity,HomeActivity::class.java))
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
                                showMessage(task.exception?.message.toString())
                        }
                } else {
                    showMessage("Algo salió mal")
                }
            }
        }
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}