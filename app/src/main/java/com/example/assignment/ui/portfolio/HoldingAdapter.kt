package com.example.assignment.ui.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.data.model.HoldingData
import com.example.assignment.databinding.HoldingListBinding
import com.example.assignment.utils.formatAsCurrency

class HoldingAdapter : RecyclerView.Adapter<HoldingAdapter.ViewHolder>() {

    private var holdingList = emptyList<HoldingData>()

    class ViewHolder(val binding: HoldingListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HoldingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setItems(newList: List<HoldingData>) {
        val diffResult = DiffUtil.calculateDiff(HoldingDiffCallback(holdingList, newList))
        holdingList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = holdingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = holdingList[position]
        with(holder.binding) {
            val context = holder.itemView.context
            lblSymbol.text = item.symbol
            lblLtp.text = context.formatAsCurrency(item.ltp)
            lblNetQty.text = String.format(item.quantity.toString())
            lblPnL.text = context.formatAsCurrency(item.pnl)
            val color = if (item.pnl > 0) R.color.green else R.color.red
            lblPnL.setTextColor(ContextCompat.getColor(context, color))
        }
    }

    class HoldingDiffCallback(
        private val oldList: List<HoldingData>,
        private val newList: List<HoldingData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Assuming 'symbol' is a unique identifier for HoldingData
            return oldList[oldItemPosition].symbol == newList[newItemPosition].symbol
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Compare all properties to check if the content is the same
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}