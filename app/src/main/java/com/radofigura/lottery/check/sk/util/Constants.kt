package com.radofigura.lottery.check.sk.util

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.radofigura.lottery.check.sk.model.Metadata

object Constants {
    var SHOW_START_TIME = "02:22:00"
    var SHOW_ONLINE_DAY_OF_WEEK = 6
    var SHOW_DURATION_HOURS = 2

    var MINUTES_TO_WAIT = 15

    var MAX_DELAY_READ_DATA = 5000L

    var FILTER_USER_BY_AMOUNT_OF_PHRASES_OWNED = 2
    var FILTER_OLDEST_POSSIBLE_TIME_IN_MINUTES = 30

    var APP_VERSION = "0.0.0"

    val READ_TIMEOUT = 30
    val WRITE_TIMEOUT = 30
    val CONNECTION_TIMEOUT = 10
    val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB

    val REMOTE_CONFIG_INTERVAL_SECONDS = 10L

    fun updateConstantsBasedOnMetadata(meta: Metadata) {
        Log.d("METADATA", meta.toString())
        SHOW_START_TIME = meta.showStartTime
        SHOW_ONLINE_DAY_OF_WEEK = meta.showRuntimeDayOfTheWeek
        SHOW_DURATION_HOURS = meta.showRuntimeDurationHours
        MINUTES_TO_WAIT = meta.minutesToPersistData
        MAX_DELAY_READ_DATA = meta.maxDelayToReadData
        FILTER_OLDEST_POSSIBLE_TIME_IN_MINUTES = meta.filterOldestPossiblePhraseInMinutes
        FILTER_USER_BY_AMOUNT_OF_PHRASES_OWNED = meta.filterUserByAmountOfPhrasesOwned
        APP_VERSION = meta.appVersion
    }

    fun getConstantBasedOnRemoteConfig(remoteConfig: FirebaseRemoteConfig): Metadata {
        return Metadata (
            showStartTime = remoteConfig.getString("showStartTime"),
            showRuntimeDayOfTheWeek = remoteConfig.getLong("showRuntimeDayOfTheWeek").toInt(),
            showRuntimeDurationHours = remoteConfig.getLong("showRuntimeDurationHours").toInt(),
            minutesToPersistData = remoteConfig.getLong("minutesToPersistData").toInt(),
            filterUserByAmountOfPhrasesOwned = remoteConfig.getLong("filterUserByAmountOfPhrasesOwned").toInt(),
            filterOldestPossiblePhraseInMinutes = remoteConfig.getLong("filterOldestPossiblePhraseInMinutes").toInt(),
            maxDelayToReadData = remoteConfig.getLong("maxDelayToReadData"),
            appVersion = remoteConfig.getString("appVersion")
        )
    }

    fun updateConstant(remoteConfig: FirebaseRemoteConfig) {
        updateConstantsBasedOnMetadata(getConstantBasedOnRemoteConfig(remoteConfig))
    }
}