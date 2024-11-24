package com.example.assignment.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.model.HoldingData
import com.example.assignment.data.model.UserHoldingItem
import com.example.assignment.data.model.toHoldingData
import com.example.assignment.data.repository.NetworkRepository
import com.example.assignment.data.source.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class PortfolioVm(private val networkRepository: NetworkRepository) : ViewModel() {

    private val _portfolioState = MutableStateFlow(PortfolioState(isLoading = true))
    val portfolioState: StateFlow<PortfolioState> = _portfolioState.asStateFlow()

//    private val _holdingList = MutableStateFlow<List<HoldingData>?>(null)
//    val holdingList: StateFlow<List<HoldingData>?> get() = _holdingList.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading get() = _isLoading.asStateFlow()

    init {
        fetchHoldingData()
    }

    private fun fetchHoldingData() {
        viewModelScope.launch {
            networkRepository.fetchHoldingData().onEach {
                when (it) {
                    is Resource.Loading -> {
                        _portfolioState.value = PortfolioState(isLoading = true)
                    }

                    is Resource.Success -> {
                        it.data?.let { list -> setData(list) }
                    }

                    is Resource.Error -> {
                        _portfolioState.value = PortfolioState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun setData(list: List<UserHoldingItem>) {
        val item = list.toHoldingData()
        val currentVal = item.sumOf { it.ltp * it.quantity }
        val totalInv = item.sumOf { it.avgPrice * it.quantity }
        val todayPnL = item.sumOf { it.pnl }
        val profitLoss = currentVal - totalInv
        val profitLossPercent = ((profitLoss / totalInv) * 100).absoluteValue
        _portfolioState.value = PortfolioState(
            holdingList = list.toHoldingData(),
            isLoading = false,
            currentVal = currentVal,
            totalInv = totalInv,
            todayPnL = todayPnL,
            profitLoss = profitLoss,
            profitLossPercent = profitLossPercent,
        )
    }
}

data class PortfolioState(
    val holdingList: List<HoldingData> = emptyList(),
    val isLoading: Boolean = false,
    val currentVal: Double = 0.0,
    val totalInv: Double = 0.0,
    val todayPnL: Double = 0.0,
    val profitLoss: Double = 0.0,
    val profitLossPercent: Double = 0.0
)