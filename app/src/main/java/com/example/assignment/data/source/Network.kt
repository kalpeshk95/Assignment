package com.example.assignment.data.source

import com.example.assignment.data.model.PortfolioResponse
import retrofit2.http.GET

interface Network {

    @GET("/")
    suspend fun fetchHoldingData(): PortfolioResponse
}