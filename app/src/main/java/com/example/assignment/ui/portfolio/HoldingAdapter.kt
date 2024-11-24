package com.example.assignment.ui.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.data.model.HoldingData
import com.example.assignment.databinding.HoldingListBinding
import com.example.assignment.utils.toAmount

class HoldingAdapter : RecyclerView.Adapter<HoldingAdapter.ViewHolder>() {

    private var employeeList = listOf<HoldingData>()

    class ViewHolder(val binding: HoldingListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HoldingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setItems(listItems: List<HoldingData>) {
        employeeList = listItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = employeeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = employeeList[position]
        with(holder.binding) {
            val context = holder.itemView.context
            lblSymbol.text = item.symbol
            lblLtp.text = context.toAmount(item.ltp)
            lblNetQty.text = String.format(item.quantity.toString())
            lblPnL.text = context.toAmount(item.pnl)
            val color = if (item.pnl > 0) R.color.green else R.color.red
            lblPnL.setTextColor(ContextCompat.getColor(context, color))
        }
    }
}
