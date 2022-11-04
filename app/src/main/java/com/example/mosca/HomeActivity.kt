package com.example.mosca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mosca.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeActivity: AppCompatActivity()  {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var expensesAdapter: ExpensesAdapter
    private var budget = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expensesAdapter = ExpensesAdapter(mutableListOf())
        binding.rvExpenses.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = expensesAdapter
        }

        binding.btnAdd.setOnClickListener {
            if (binding.etDescription.text.toString()
                    .isNotBlank() && binding.etAmount.text.toString().isNotBlank()
            ) {
                val expense = Expense(
                    description = binding.etDescription.text.toString().trim(),
                    amount = binding.etAmount.text.toString().trim().toDouble()
                )
                addExpenseAuto(expense)
            }
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
            Expense(4,"Entertainment", -1000.00))

        data.forEach{expense ->
            addExpenseAuto(expense)
        }
    }

    private fun addExpenseAuto(expense: Expense) {
        budget += expense.amount
        binding.tvAmount.text = "$ $budget"
        expensesAdapter.add(expense)
    }

    private fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}