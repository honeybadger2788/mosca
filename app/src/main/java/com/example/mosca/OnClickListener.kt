package com.example.mosca

import com.example.mosca.model.Expense

interface OnClickListener {
    fun onLongClick(expense: Expense, currentAdapter: ExpensesAdapter)
}