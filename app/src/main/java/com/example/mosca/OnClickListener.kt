package com.example.mosca

interface OnClickListener {
    fun onLongClick(expense: Expense, currentAdapter: ExpensesAdapter)
}