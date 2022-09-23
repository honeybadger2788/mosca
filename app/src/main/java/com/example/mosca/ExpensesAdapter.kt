package com.example.mosca

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mosca.databinding.ItemExpenseBinding

class ExpensesAdapter(var expenseList: MutableList<Expense>):
    RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenseList.get(position)

        holder.binding.tvCategory.text = expense.category
        holder.binding.tvCategoryAmount.text = expense.amount.toString()
    }

    override fun getItemCount(): Int = expenseList.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemExpenseBinding.bind(view)
    }

    fun add(expense: Expense) {
        expenseList.add(expense)
        notifyDataSetChanged()
    }
}