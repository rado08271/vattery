package com.radofigura.lottery.check.sk.viewmodel

import androidx.lifecycle.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.radofigura.lottery.check.sk.dao.TicketsDao
import com.radofigura.lottery.check.sk.model.Phrase
import com.radofigura.lottery.check.sk.model.Ticket
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.service.actions.GetLotteryWinners
import com.radofigura.lottery.check.sk.service.actions.GetPhraseBasedOnTime
import com.radofigura.lottery.check.sk.util.Constants
import com.radofigura.lottery.check.sk.util.DateUtils
import com.radofigura.lottery.check.sk.util.PhraseFilteringManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ticketsDao: TicketsDao,
    private val getPhrasesBasedOnTime: GetPhraseBasedOnTime,
    private val getLotteryWinners: GetLotteryWinners,
    private val remoteConfigData: FirebaseRemoteConfig) : ViewModel() {

    val currentPhaseBasedOnTimeLiveData = MutableLiveData<Phrase?>(null)
    val allLotteryTicketsLiveData = MutableLiveData<MutableList<Ticket>>(null)
    val filteredWinningTicketsViewMode = MutableLiveData<MutableList<Ticket>>(null)

    // FIXME: This is just quickfix solution but should definitely be fixed
    var shouldContinue = false

    fun init() {
        viewModelScope.launch {
            shouldContinue = true
            getLotteryTickets()
            getRemoteConfigData()

            getAllPhrasesBasedOnGivenTime()
                .collect {
                    when (it) {
                        is State.LoadingState -> {

                        }
                        is State.ErrorState -> {

                        }
                        is State.DataState -> {
                            val phrase = PhraseFilteringManager.filterPhrase(it.data)
                            currentPhaseBasedOnTimeLiveData.value = phrase
                        }
                    }
                }
        }
    }

    private fun getRemoteConfigData() {
        remoteConfigData
            .fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Constants.updateConstant(remoteConfigData)
                }
            }
    }

    private fun getAllPhrasesBasedOnGivenTime() = flow {
        emit(State.LoadingState)

        while (shouldContinue) {
            if (DateUtils.shouldReceiveData()) {
                try {
                    emit(
                        State.DataState(
                            getPhrasesBasedOnTime(
                                Date((DateUtils.getCurrentTime().time - (Constants.MINUTES_TO_WAIT * 60 * 1000)))
                            )
                        )
                    )
                } catch (e: Exception) {
                    emit(State.ErrorState(e))
                }
            } else {
                emit(State.DataState(arrayListOf<Phrase>()))
            }

            delay(Constants.MAX_DELAY_READ_DATA)
        }

    }

    suspend fun filterLotteryWinners(myTickets: List<Ticket>) {
        filteredWinningTicketsViewMode.value = (null)

        try {
            val winners = getLotteryWinners()

            for (ticket in myTickets) {
                val winner = winners.firstOrNull {
                    it.ticketId.equals(ticket.ticketId)
                }

                if (winner != null) {
                    ticket.won = true
                    ticket.prize = winner.winningPrize.toInt()
                    updateTicket(ticket)
                }
            }

            filteredWinningTicketsViewMode.value = myTickets.toMutableList()
        } catch (e: Exception) {
            filteredWinningTicketsViewMode.value = (null)
        }
    }

    fun getLotteryTickets () {
        allLotteryTicketsLiveData.value = (null)

        allLotteryTicketsLiveData.value = (
            ticketsDao.getLotteryTickets().toMutableList()
        )
    }

    fun updateTicket(ticket: Ticket) {
        ticketsDao.updateLotteryTicket(ticket)
    }

}