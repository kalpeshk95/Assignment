package com.example.assignment.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.R
import com.example.assignment.databinding.FragmentPortfolioBinding
import com.example.assignment.utils.formatAsCurrency
import com.example.assignment.utils.gone
import com.example.assignment.utils.setTextColorRes
import com.example.assignment.utils.visible
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PortfolioFragment : Fragment(R.layout.fragment_portfolio) {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PortfolioVm by viewModel()
    private val holdingsAdapter by lazy { HoldingAdapter() }

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
        with(binding.rvHoldings) {
            layoutManager = LinearLayoutManager(context)
            adapter = holdingsAdapter
        }
    }

    private fun observer() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.portfolioState.collect { state ->
                        showLoading(state)
                        handleError(state)
                        showSuccess(state)
                    }
                }
            }
        }
    }

    private fun showLoading(state: PortfolioUiState) {
        with(binding) {
            if (state is PortfolioUiState.Loading) {
                bottomView.main.gone()
                progressBar.visible()
            } else {
                progressBar.gone()
                bottomView.main.visible()
            }
        }
    }

    private fun showSuccess(state: PortfolioUiState) {
        if (state is PortfolioUiState.Success) {
            state.holdingList.let { holdingList ->
                holdingsAdapter.setItems(holdingList)
                val todayPnLColor = if (state.todayPnL >= 0) R.color.green else R.color.red
                val profitLossColor = if (state.profitLoss >= 0) R.color.green else R.color.red
                with(binding) {
                    progressBar.gone()
                    errorView.root.gone()
                    viewGroup.visible()
                    with(bottomView) {
                        lblCurrentVal.text = requireContext().formatAsCurrency(state.currentVal)
                        lblTotalInv.text = requireContext().formatAsCurrency(state.totalInv)
                        lblTodayPnL.text = requireContext().formatAsCurrency(state.todayPnL)
                        lblTodayPnL.setTextColorRes(todayPnLColor)
                        lblProfitLoss.text = requireContext().formatAsCurrency(state.profitLoss)
                        lblProfitLossPer.text =
                            getString(R.string.lbl_percentage, state.profitLossPercent)
                        lblProfitLoss.setTextColorRes(profitLossColor)
                        lblProfitLossPer.setTextColorRes(profitLossColor)
                    }
                }
            }
        }
    }

    private fun handleError(state: PortfolioUiState) {
        if (state is PortfolioUiState.Error) {
            with(binding) {
                progressBar.gone()
                viewGroup.gone()
                errorView.root.visible()
                errorView.btnError.setOnClickListener {
                    if (state.isRetryable) viewModel.refresh()
                }
                errorView.tvError.text = state.message
            }
        }
    }

    private fun initClick() {
        with(binding.bottomView) {
            txtProfitLoss.setOnClickListener {
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

        binding.errorView.btnError.setOnClickListener {
            viewModel.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}