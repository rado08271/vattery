package com.radofigura.lottery.check.sk.viewmodel

import androidx.lifecycle.ViewModel
import com.radofigura.lottery.check.sk.model.Winner
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.service.actions.CreatePhrase
import com.radofigura.lottery.check.sk.service.actions.GetLotteryWinners
import com.radofigura.lottery.check.sk.service.actions.GetPhraseBasedOnTime
import com.radofigura.lottery.check.sk.util.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CheckWinnersViewModel @Inject constructor(
    private val getLotteryWinners: GetLotteryWinners
) : ViewModel() {

    fun getAllLotteryWinners() = flow {
        emit(State.LoadingState)

        try {
            delay(1000)
            emit(State.DataState(getLotteryWinners()))
        } catch (e: Exception) {
            emit(State.ErrorState(e))
        }
    }

    fun filterWinnersQuery(queryString: String, winners: List<Winner>) = flow {
        delay(100)

        emit(winners.filter {
            StringUtils.deAccent("${it.firstName} ${it.city} ${it.ticketId}")
                .contains(queryString)
        })
    }

}