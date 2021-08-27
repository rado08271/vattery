package com.radofigura.lottery.check.sk.service.api

import com.google.gson.JsonArray
import com.radofigura.lottery.check.sk.model.Winner
import retrofit2.http.GET

interface MfApi {
    @GET("/components/mfsrweb/winners/data-ajax.jsp")
    suspend fun getAllLotteryWinners(): ArrayList<Winner>

}