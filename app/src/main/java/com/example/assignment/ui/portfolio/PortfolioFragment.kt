package com.example.assignment.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.R
import com.example.assignment.databinding.FragmentPortfolioBinding
import com.example.assignment.utils.gone
import com.example.assignment.utils.toAmount
import com.example.assignment.utils.visible
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PortfolioFragment : Fragment(R.layout.fragment_portfolio) {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PortfolioVm>()
    private val holdingAdapter by lazy { HoldingAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClick()
        observer()
    }

    private fun initView() {
        with(binding.rvHolding) {
            layoutManager = LinearLayoutManager(context)
            adapter = holdingAdapter
        }
    }

    private fun observer() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.portfolioState.collect { state ->
                setBottomData(state) // Update UI with state
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        with(binding) {
            if (loading) {
                bottomView.gone()
                progressBar.visible()
            } else {
                progressBar.gone()
                bottomView.visible()
            }
        }
    }

    private fun setBottomData(it: PortfolioState) {
        showLoading(it.isLoading)
        holdingAdapter.setItems(it.holdingList)
        val todayPnLColor = if (it.todayPnL > 0) R.color.green else R.color.red
        val profitLossColor = if (it.profitLoss > 0) R.color.green else R.color.red
        with(binding) {
            lblCurrentVal.text = requireContext().toAmount(it.currentVal)
            lblTotalInv.text = requireContext().toAmount(it.totalInv)
            lblTodayPnL.text = requireContext().toAmount(it.todayPnL)
            lblTodayPnL.setTextColor(ContextCompat.getColor(requireContext(), todayPnLColor))
            lblProfitLoss.text = requireContext().toAmount(it.profitLoss)
            lblProfitLossPer.text = getString(R.string.lbl_percentage, it.profitLossPercent)
            lblProfitLoss.setTextColor(ContextCompat.getColor(requireContext(), profitLossColor))
            lblProfitLossPer.setTextColor(ContextCompat.getColor(requireContext(), profitLossColor))
        }
    }

    private fun initClick() {
        with(binding) {
            binding.txtProfitLoss.setOnClickListener {
                if (infoGroup.isGone) {
                    infoGroup.visible()
                    txtProfitLoss.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_down,
                        0
                    )
                } else {
                    infoGroup.gone()
                    txtProfitLoss.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_arrow_up,
                        0
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}