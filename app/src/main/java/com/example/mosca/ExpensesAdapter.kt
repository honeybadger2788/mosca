package com.example.mosca

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.mosca.databinding.ItemExpenseBinding
import kotlin.math.sign

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

        holder.binding.tvCategory.text = expense.description
        holder.binding.tvCategoryAmount.text = expense.amount.toString()
        if (expense.amount < 0){
            val outcomeIcon = getDrawable(context,R.drawable.ic_arrow_downward)
            holder.binding.imgCategory.setImageDrawable(outcomeIcon)
            holder.binding.imgCategory.setColorFilter(getColor(context,R.color.red))
        } else {
            val incomeIcon = getDrawable(context,R.drawable.ic_arrow_upward)
            holder.binding.imgCategory.setImageDrawable(incomeIcon)
            holder.binding.imgCategory.setColorFilter(getColor(context,R.color.green))
        }
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