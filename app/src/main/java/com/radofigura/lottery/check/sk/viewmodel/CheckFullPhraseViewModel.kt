package com.radofigura.lottery.check.sk.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radofigura.lottery.check.sk.model.Phrase
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.service.actions.CreatePhrase
import com.radofigura.lottery.check.sk.service.actions.GetLotteryWinners
import com.radofigura.lottery.check.sk.service.actions.GetPhraseBasedOnTime
import com.radofigura.lottery.check.sk.util.Constants
import com.radofigura.lottery.check.sk.util.DateUtils
import com.radofigura.lottery.check.sk.util.PhraseFilteringManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CheckFullPhraseViewModel @Inject constructor(
    private val getPhrasesBasedOnTime: GetPhraseBasedOnTime
) : ViewModel() {
    val currentPhasesBasedOnTimeLiveData = MutableLiveData<ArrayList<Phrase>?>(null)

    init {
        viewModelScope.launch {
            getAllPhrasesBasedOnGivenTime()
                .collect {
                    when (it) {
                        is State.LoadingState -> {

                        }
                        is State.ErrorState -> {

                        }
                        is State.DataState -> {
                            currentPhasesBasedOnTimeLiveData.value = it.data
                        }
                    }
                }
        }
    }

    private fun getAllPhrasesBasedOnGivenTime() = flow {
        emit(State.LoadingState)

        while (true) {
            if (DateUtils.shouldReceiveData()) {
                try {
                    emit(State.DataState(
                        getPhrasesBasedOnTime(
                            Date((DateUtils.getCurrentTime().time - (Constants.MINUTES_TO_WAIT * 60 * 1000)))
                        )
                    ))
                } catch (e: Exception) {
                    emit(State.ErrorState(e))
                }
            } else {
                emit(State.DataState(arrayListOf<Phrase>()))
            }

            delay(Constants.MAX_DELAY_READ_DATA)
        }
    }

}