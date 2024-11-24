package com.example.assignment.data.repository

import com.example.assignment.data.model.UserHoldingItem
import com.example.assignment.data.source.Network
import com.example.assignment.data.source.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NetworkManager {
    fun fetchHoldingData(): Flow<Resource<List<UserHoldingItem>?>>
}

class NetworkRepository(private val network: Network) : NetworkManager {

    override fun fetchHoldingData(): Flow<Resource<List<UserHoldingItem>?>> = flow {
        emit(Resource.Loading)
        try {
            val holdingData = network.fetchHoldingData().data?.userHolding
            emit(Resource.Success(holdingData))

        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

}
