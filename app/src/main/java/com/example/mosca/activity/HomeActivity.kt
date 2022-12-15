package com.example.mosca.activity

import android.content.Intent
import android.os.Bundle
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
    private val currentUser = Firebase.auth.currentUser?.email.toString()

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
        with(binding){
            btnAdd.setOnClickListener {
                if (etDescription.text.toString().isNotBlank()
                    && etAmount.text.toString().isNotBlank()) {
                    createExpense(etDescription.text.toString().trim(),etAmount.text.toString().trim().toDouble())
                } else if (etDescription.text.toString().isBlank())
                    etDescription.error = getString(R.string.validation_field_required)
                else
                    etAmount.error = getString(R.string.validation_field_required)
            }
        }
    }

    // Error: se duplican los valores al volver atrás
    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData() {
        db.collection(getString(R.string.key_user_collection)).document(currentUser)
            .collection(getString(R.string.key_expense_collection)).get()
            .addOnSuccessListener { data ->
                if (data != null) {
                    println(data.documents)
                    data.documents.forEach { document ->
                        addExpenseAuto(Expense(
                            document.id,
                            document.get(getString(R.string.key_description_field)) as String,
                            document.get(getString(R.string.key_amount_field)) as Double))
                    }
                } else {
                    showMessage(getString(R.string.msg_error_response))
                }
            }
            .addOnFailureListener { exception ->
                showMessage(exception.toString())
            }
    }

    private fun createExpense(description: String, amount: Double) {
        with(binding){
            db.collection(getString(R.string.key_user_collection)).document(currentUser)
                .collection(getString(R.string.key_expense_collection))
                .add(hashMapOf(getString(R.string.key_description_field) to description,
                    getString(R.string.key_amount_field) to amount))
                .addOnSuccessListener {
                    val expense = Expense(
                        uid = it.id,
                        description = description,
                        amount = amount
                    )
                    addExpenseAuto(expense)
                    showMessage(getString(R.string.msg_success_response))
                    etDescription.text?.clear()
                    etAmount.text?.clear()
                }
                .addOnFailureListener { e ->
                    showMessage(e.message.toString())
                }
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
            .setTitle(getString(R.string.alert_delete_title))
            .setPositiveButton(getString(R.string.text_accept)) { _, _ ->
                db.collection(getString(R.string.key_user_collection)).document(currentUser)
                    .collection(getString(R.string.key_expense_collection)).document(expense.uid).delete()
                    .addOnSuccessListener {
                        deleteExpenseAuto(expense)
                        showMessage(getString(R.string.msg_success_response))
                    }
            }
            .setNegativeButton(getString(R.string.text_cancel),null)

        builder.create().show()
    }

    override fun onClick(expense: Expense, currentAdapter: ExpensesAdapter) {
        val builder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.alert_edit_title))
            .setPositiveButton(getString(R.string.text_accept)) { _, _ ->
                binding.etDescription.setText(expense.description)
                binding.etAmount.setText(expense.amount.toString())
            }
            .setNegativeButton(getString(R.string.text_cancel),null)

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
                intent.putExtra(getString(R.string.key_email), currentUser)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}