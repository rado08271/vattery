package com.radofigura.lottery.check.sk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ticket (
//    @PrimaryKey(autoGenerate = true)
//    val id: Int?,
    @PrimaryKey
    @ColumnInfo(name = "ticket_id")
    var ticketId: String = "",
    @ColumnInfo(name = "won")
    var won: Boolean = false,
    @ColumnInfo(name = "prize_won")
    var prize: Int = -1
)