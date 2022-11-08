package com.example.mosca.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mosca.AuthActivity
import com.example.mosca.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registerNewAccount()
    }

    private fun registerNewAccount() {
        with(binding){
            btnRegister.setOnClickListener {
                if (etPassword.text.toString() == etConfirmPassword.text.toString()){
                    if (etEmail.text!!.isNotBlank()
                        && etPassword.text!!.isNotBlank()) {
                        val email = etEmail.text.toString()
                        val password = etPassword.text.toString()

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    db.collection("users")
                                        .document(email).set(hashMapOf(
                                        "rol" to "user",
                                    ))
                                        .addOnSuccessListener {
                                            startActivity(Intent(this@RegisterActivity,
                                                AuthActivity::class.java))
                                    }
                                        .addOnFailureListener { e ->
                                            showMessage(e.message.toString())
                                        }
                                } else {
                                    showMessage(task.exception?.message.toString())
                                }
                            }
                    }
                } else {
                    showMessage("Las contraseñas no coinciden")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}