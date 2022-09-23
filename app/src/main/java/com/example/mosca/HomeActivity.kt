package com.example.mosca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mosca.databinding.ActivityHomeBinding

class HomeActivity: AppCompatActivity()  {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var expensesAdapter: ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expensesAdapter = ExpensesAdapter(mutableListOf())
        binding.rvExpenses.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = expensesAdapter
        }

        binding.btnAdd.setOnClickListener{
            startActivity(Intent(this, ExpenseFormActivity::class.java))
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
            Expense(3,"Entertainment", -1000.00))

        data.forEach{expense ->
            addExpenseAuto(expense)
        }
    }

    private fun addExpenseAuto(expense: Expense) {
        expensesAdapter.add(expense)
    }
}