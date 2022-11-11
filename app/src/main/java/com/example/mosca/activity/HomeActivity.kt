package com.example.mosca.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mosca.ExpensesAdapter
import com.example.mosca.OnClickListener
import com.example.mosca.R
import com.example.mosca.databinding.ActivityHomeBinding
import com.example.mosca.model.Expense
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity: AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var expensesAdapter: ExpensesAdapter
    private val db = Firebase.firestore
    private var budget = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        expensesAdapter = ExpensesAdapter(mutableListOf(),this)
        binding.rvExpenses.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = expensesAdapter
        }

        // POR AHORA SOLO CREA DOCUMENTOS NUEVOS, NO EDITA EXISTENTES
        binding.btnAdd.setOnClickListener {
            if (binding.etDescription.text.toString().isNotBlank()
                && binding.etAmount.text.toString().isNotBlank()) {
                db.collection("users").document(auth.currentUser!!.email.toString())
                    .collection("expenses")
                    .add(hashMapOf("description" to binding.etDescription.text.toString().trim(),
                        "amount" to binding.etAmount.text.toString().trim().toDouble()))
                    .addOnSuccessListener {
                        val expense = Expense(
                            uid = it.id,
                            description = binding.etDescription.text.toString().trim(),
                            amount = binding.etAmount.text.toString().trim().toDouble()
                        )
                        addExpenseAuto(expense)
                        showMessage("Operación exitosa")
                        binding.etDescription.text?.clear()
                        binding.etAmount.text?.clear()
                    }
                    .addOnFailureListener { e ->
                        showMessage(e.message.toString())
                    }
            } else if (binding.etDescription.text.toString().isBlank())
                binding.etDescription.error = getString(R.string.validation_field_required)
            else
                binding.etAmount.error = getString(R.string.validation_field_required)
        }
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData() {
        db.collection("users").document(auth.currentUser!!.email.toString())
            .collection("expenses").get()
            .addOnSuccessListener { data ->
                if (data != null) {
                    println(data.documents)
                    data.documents.forEach { document ->
                        addExpenseAuto(Expense(
                            document.id,
                            document.get("description") as String,
                            document.get("amount") as Double))
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun addExpenseAuto(expense: Expense) {
        budget += expense.amount
        binding.tvAmount.text = "$ $budget"
        expensesAdapter.add(expense)
    }

    private fun deleteExpenseAuto(expense: Expense) {
        budget -= expense.amount
        binding.tvAmount.text = "$ $budget"
       expensesAdapter.remove(expense)
    }

    override fun onLongClick(expense: Expense, currentAdapter: ExpensesAdapter) {
        val builder = AlertDialog.Builder(this)
            .setTitle("¿Eliminar gasto?")
            .setPositiveButton("Aceptar",{ dialogInterface, i ->
                db.collection("users").document(auth.currentUser!!.email.toString())
                    .collection("expenses").document(expense.uid).delete()
                    .addOnSuccessListener {
                        deleteExpenseAuto(expense)
                        showMessage("Eliminado exitosamente")
                    }
            })
            .setNegativeButton("Cancelar",null)

        builder.create().show()
    }

    override fun onClick(expense: Expense, currentAdapter: ExpensesAdapter) {
        val builder = AlertDialog.Builder(this)
            .setTitle("¿Editar gasto?")
            .setPositiveButton("Aceptar",{ dialogInterface, i ->
                db.collection("users").document(auth.currentUser!!.email.toString())
                    .collection("expenses").document(expense.uid).get()
                    .addOnSuccessListener {
                        binding.etDescription.setText(it.getString("description"))
                        binding.etAmount.setText(it.getDouble("amount").toString())
                    }
            })
            .setNegativeButton("Cancelar",null)

        builder.create().show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout -> {
                auth.signOut()
                onBackPressedDispatcher.onBackPressed()
            }
            R.id.action_profile -> {
                val intent = Intent(this@HomeActivity, UpdatePasswordActivity::class.java)
                intent.putExtra("email", auth.currentUser!!.email.toString())
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}