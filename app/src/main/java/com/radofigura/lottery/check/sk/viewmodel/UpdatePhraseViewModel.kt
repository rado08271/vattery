package com.radofigura.lottery.check.sk.viewmodel

import androidx.lifecycle.ViewModel
import com.radofigura.lottery.check.sk.model.Phrase
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.service.actions.CreatePhrase
import com.radofigura.lottery.check.sk.service.actions.GetLotteryWinners
import com.radofigura.lottery.check.sk.service.actions.GetPhraseBasedOnTime
import com.radofigura.lottery.check.sk.util.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class UpdatePhraseViewModel @Inject constructor(
    private val createPhrase: CreatePhrase,
    private val getPhrasesBasedOnTime: GetPhraseBasedOnTime,
    private val getLotteryWinners: GetLotteryWinners
) : ViewModel() {

    fun createNewPhrase(phrase: String, senderId: String?) = flow {
        emit(State.LoadingState)

        try {
            delay(1000)
            emit(
                State.DataState(createPhrase(
                Phrase(phrase = StringUtils.deAccent(phrase), senderId = senderId ?: "")
            )))
        } catch (e: Exception) {
            emit(State.ErrorState(e))
        }
    }

}