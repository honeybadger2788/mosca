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
import com.example.mosca.UpdatePasswordActivity
import com.example.mosca.R
import com.example.mosca.databinding.ActivityHomeBinding
import com.example.mosca.model.Expense
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity: AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var expensesAdapter: ExpensesAdapter
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

        binding.btnAdd.setOnClickListener {
            if (binding.etDescription.text.toString().isNotBlank()
                && binding.etAmount.text.toString().isNotBlank()) {
                val expense = Expense(
                    description = binding.etDescription.text.toString().trim(),
                    amount = binding.etAmount.text.toString().trim().toDouble()
                )
                addExpenseAuto(expense)
                binding.etDescription.text?.clear()
                binding.etAmount.text?.clear()
                showMessage("Operación exitosa")
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
        val data = mutableListOf(
            Expense(1, "Food", -5000.00),
            Expense(2,"Home", -10000.00),
            Expense(3,"Salary", 10000.00),
            Expense(4,"Entertainment", -1000.00)
        )

        data.forEach{expense ->
            addExpenseAuto(expense)
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
                deleteExpenseAuto(expense)
                showMessage("Eliminado exitosamente")
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
                val intent = Intent(this@HomeActivity,UpdatePasswordActivity::class.java)
                intent.putExtra("email", auth.currentUser!!.email)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}