package com.radofigura.lottery.check.sk.model

import com.radofigura.lottery.check.sk.util.DateUtils
import java.util.*

data class Phrase (
    val id: String = UUID.randomUUID().toString(),
    val senderId: String = "",
    val phrase: String = "",
    val dateAdded: Date = DateUtils.getCurrentTime()!!
)

