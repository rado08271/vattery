package com.radofigura.lottery.check.sk.util

import com.radofigura.lottery.check.sk.model.Phrase
import java.util.*

object PhraseFilteringManager {

    fun filterPhrase(receivedPhrases: List<Phrase>): Phrase? {
        var phrasesToFilter = receivedPhrases.toMutableList()

        if (receivedPhrases.isNullOrEmpty()) {
            return null;
        }

        var senders = mutableMapOf<String, Int>()

        for (phrase in receivedPhrases) {
            senders.put(phrase.senderId, (senders.get(phrase.senderId) ?: 0) +1)
        }

        // Filter senders first
        for (element in senders) {
            // Half of phrases sent are from this user! and there are more than 2 users
            if (senders.size > (Constants.FILTER_USER_BY_AMOUNT_OF_PHRASES_OWNED +1) && element.value > receivedPhrases.size /Constants.FILTER_USER_BY_AMOUNT_OF_PHRASES_OWNED) {
                phrasesToFilter.removeAll {
                    it.senderId == element.key
                }
            }
        }

        var phrases = mutableMapOf<String, Int>()
        var mostRecent = Date(0)

        // Filter phrases with cleaned spammers
        for (phrase in phrasesToFilter) {
            phrases.put(phrase.phrase, (phrases.get(phrase.phrase) ?: 0) +1)

            if (phrase.dateAdded.after(mostRecent)) {
                mostRecent = phrase.dateAdded
            }
        }

        var phraseWithMostResults = 0
        var phrase: Phrase? = null

        for (element in phrases) {
            if (element.value > phraseWithMostResults) {
                phraseWithMostResults = element.value

                // find current phrase with most values, it must not be older than
                phrase = phrasesToFilter.first {
                    var timeDiff = ((mostRecent.time - (Constants.FILTER_OLDEST_POSSIBLE_TIME_IN_MINUTES * 60 * 1000)) - it.dateAdded.time)
                    it.phrase == element.key && (mostRecent.time - (Constants.FILTER_OLDEST_POSSIBLE_TIME_IN_MINUTES * 60 * 1000)) - it.dateAdded.time < 0
                }
            }
        }

        return phrase
    }

    fun getNewestPhrase(receivedPhrases: List<Phrase>): Phrase? {
        var mostRecent = Date(0)
        var phrase: Phrase? = null

        receivedPhrases.forEach {
            if (it.dateAdded.after(mostRecent)) {
                mostRecent = it.dateAdded
                phrase = it
            }
        }

        return phrase
    }
}