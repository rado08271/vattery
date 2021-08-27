package com.radofigura.lottery.check.sk.dao

import androidx.room.*
import com.radofigura.lottery.check.sk.model.Ticket

@Dao
interface TicketsDao {
    @Query("SELECT * FROM Ticket;")
    fun getLotteryTickets(): List<Ticket>

    @Query("SELECT * FROM Ticket WHERE ticket_id = :ticketId;")
    fun getLotteryTicketByTicketID(ticketId: String): Ticket

    @Insert
    fun addLotteryTicket(ticket: Ticket)

    @Delete
    fun deleteLotteryTicket(ticket: Ticket)

    @Update
    fun updateLotteryTicket(ticket: Ticket)
}