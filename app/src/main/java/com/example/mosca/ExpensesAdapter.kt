package com.example.mosca

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.mosca.databinding.ItemExpenseBinding
import com.example.mosca.model.Expense

class ExpensesAdapter(var expenseList: MutableList<Expense>, private val listener: OnClickListener):
    RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenseList.get(position)

        holder.setListener(expense)

        with(holder.binding){
            tvExpense.text = expense.description
            tvExpenseAmount.text = expense.amount.toString()
            if (expense.amount < 0){
                val outcomeIcon = getDrawable(context,R.drawable.ic_arrow_downward)
                imgCategory.setImageDrawable(outcomeIcon)
                imgCategory.setColorFilter(getColor(context,R.color.red))
            } else {
                val incomeIcon = getDrawable(context,R.drawable.ic_arrow_upward)
                imgCategory.setImageDrawable(incomeIcon)
                imgCategory.setColorFilter(getColor(context,R.color.green))
            }
        }
    }

    override fun getItemCount(): Int = expenseList.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemExpenseBinding.bind(view)

        fun setListener(expense: Expense){
            binding.root.setOnLongClickListener {
                listener.onLongClick(expense, this@ExpensesAdapter)
                true
            }
            binding.root.setOnClickListener {
                listener.onClick(expense, this@ExpensesAdapter)
                true
            }
        }
    }

    fun add(expense: Expense) {
        expenseList.add(expense)
        notifyDataSetChanged()
    }

    fun remove(expense: Expense) {
        expenseList.remove(expense)
        notifyDataSetChanged()
    }

    fun edit() {
        notifyDataSetChanged()
    }
}