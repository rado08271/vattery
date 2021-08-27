package com.radofigura.lottery.check.sk.model

import com.google.gson.annotations.SerializedName

data class Winner (
    @SerializedName("kod")
    val ticketId: String,

    @SerializedName("obec")
    val city: String,

    @SerializedName("vyherna suma")
    val winningPrize: String,

    @SerializedName("meno")
    val firstName: String


)
