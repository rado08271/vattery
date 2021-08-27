package com.radofigura.lottery.check.sk.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.radofigura.lottery.check.sk.dao.TicketsDao
import com.radofigura.lottery.check.sk.model.Ticket

@Database(entities = [Ticket::class], version = 1)
abstract class TicketDatabase: RoomDatabase() {
    abstract fun ticketsDao(): TicketsDao
}