package com.radofigura.lottery.check.sk.service.actions

import com.radofigura.lottery.check.sk.model.Winner
import com.radofigura.lottery.check.sk.service.api.MfApi
import javax.inject.Inject

class GetLotteryWinners @Inject constructor(
    private val mfApi: MfApi
) {
    suspend operator fun invoke(): ArrayList<Winner> {
        return mfApi.getAllLotteryWinners()
    }
}