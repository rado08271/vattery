package com.radofigura.lottery.check.sk.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radofigura.lottery.check.sk.dao.TicketsDao
import com.radofigura.lottery.check.sk.model.Ticket
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.service.actions.GetLotteryWinners
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddLotteryTicketsViewModel @Inject constructor(
    private val ticketsDao: TicketsDao,
    private val getAllWinners: GetLotteryWinners
): ViewModel() {

    val lotteryTicketResultLiveData = MutableLiveData<State<*>>(null)
    val lotteryTicketRemoveResultLiveData = MutableLiveData<State<*>>(null)
    val allLotteryTicketsLiveData = MutableLiveData<MutableList<Ticket>>(null)
    val filteredWinningTicketsViewMode = MutableLiveData<MutableList<Ticket>>(null)

    fun addLotteryTickets(lotteryTicket: Ticket) {
        viewModelScope.launch {
            lotteryTicketResultLiveData.value = (State.LoadingState)

            try {
                lotteryTicketResultLiveData.value = (State.DataState(
                    ticketsDao.addLotteryTicket(lotteryTicket)
                ))
            } catch (e: Exception) {
                lotteryTicketResultLiveData.value = (State.ErrorState(e))
            }
        }
    }

    fun getLotteryTickets () {
            allLotteryTicketsLiveData.value = (null)

            allLotteryTicketsLiveData.value = (
                ticketsDao.getLotteryTickets().toMutableList()
            )
    }

    fun removeLotteryTicket (lotteryTicket: Ticket) {
        viewModelScope.launch {
            lotteryTicketRemoveResultLiveData.value = (State.LoadingState)

            try {
                lotteryTicketRemoveResultLiveData.value = (State.DataState(
                    ticketsDao.deleteLotteryTicket(lotteryTicket)
                ))
            } catch (e: Exception) {
                lotteryTicketRemoveResultLiveData.value = (State.ErrorState(e))
            }
        }
    }

    suspend fun filterLotteryWinners(myTickets: List<Ticket>) {
        filteredWinningTicketsViewMode.value = (null)

        try {
            val winners = getAllWinners()

            myTickets.filter { ticket ->
                winners.first {
                    it.ticketId.equals(ticket.ticketId)
                } .ticketId == ticket.ticketId
            }
        } catch (e: Exception) {
            filteredWinningTicketsViewMode.value = (null)
        }
    }
}