package com.example.assignment.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.model.HoldingData
import com.example.assignment.data.model.UserHoldingItem
import com.example.assignment.data.model.toHoldingData
import com.example.assignment.data.repository.NetworkRepository
import com.example.assignment.data.source.Resource
import com.example.assignment.utils.NetworkStatusHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class PortfolioVm(
    private val networkRepository: NetworkRepository,
    private val networkStatusHelper: NetworkStatusHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _portfolioState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val portfolioState: StateFlow<PortfolioUiState> = _portfolioState.asStateFlow()

    init {
        loadData()
    }

    fun refresh() {
        loadData()
    }

    private fun loadData() {

        viewModelScope.launch(dispatcher) {

            if (!networkStatusHelper.isNetworkAvailable.value) {
                _portfolioState.value = PortfolioUiState.Error(
                    message = "No internet connection",
                    isRetryable = true
                )
                return@launch
            }


            networkRepository.fetchHoldingData().catch { exception ->
                _portfolioState.value = PortfolioUiState.Error(
                    message = exception.message ?: "An error occurred",
                    isRetryable = true
                )
            }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _portfolioState.value = PortfolioUiState.Loading
                        }

                        is Resource.Success -> {
                            resource.data?.let { data ->
                                updateUiState(data)
                            } ?: run {
                                _portfolioState.value = PortfolioUiState.Error(
                                    message = "No data available",
                                    isRetryable = true
                                )
                            }
                        }

                        is Resource.Error -> {
                            _portfolioState.value = PortfolioUiState.Error(
                                message = resource.exception.toString(),
                                isRetryable = true
                            )
                        }
                    }
                }
        }
    }

    private fun updateUiState(holdings: List<UserHoldingItem>) {
        val holdingData = holdings.toHoldingData()
        val currentVal = holdingData.sumOf { it.ltp * it.quantity }
        val totalInv = holdingData.sumOf { it.avgPrice * it.quantity }
        val todayPnL = holdingData.sumOf { it.pnl }
        val profitLoss = currentVal - totalInv
        val profitLossPercent =
            if (totalInv > 0) ((profitLoss / totalInv) * 100).absoluteValue else 0.0

        _portfolioState.value = PortfolioUiState.Success(
            holdingList = holdingData,
            currentVal = currentVal,
            totalInv = totalInv,
            todayPnL = todayPnL,
            profitLoss = profitLoss,
            profitLossPercent = profitLossPercent,
        )
    }

    override fun onCleared() {
        super.onCleared()
        networkStatusHelper.unregister()
    }
}

sealed class PortfolioUiState {
    object Loading : PortfolioUiState()
    data class Success(
        val holdingList: List<HoldingData>,
        val currentVal: Double,
        val totalInv: Double,
        val todayPnL: Double,
        val profitLoss: Double,
        val profitLossPercent: Double
    ) : PortfolioUiState()

    data class Error(val message: String, val isRetryable: Boolean = true) : PortfolioUiState()
}