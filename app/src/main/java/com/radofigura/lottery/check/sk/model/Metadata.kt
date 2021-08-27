package com.radofigura.lottery.check.sk.model

data class Metadata (
    val showStartTime: String = "20:30:00",
    val showRuntimeDayOfTheWeek: Int = 1,
    val showRuntimeDurationHours: Int = 2,

    val minutesToPersistData: Int = 15,

    val filterUserByAmountOfPhrasesOwned: Int = 2,
    val filterOldestPossiblePhraseInMinutes: Int = 10,
    val maxDelayToReadData: Long = 5000,

    val appVersion: String = "0.0.0"
)
