package com.radofigura.lottery.check.sk.dao

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.radofigura.lottery.check.sk.model.Phrase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

@Deprecated(message = "For now this should be deprecated until unresolved callbackFlow issues!!")
class Repository  {
    val phrasesCollection = Firebase.firestore.collection("phrases")

    @ExperimentalCoroutinesApi
    fun getAllPhrasesBasedOnGivenTime(date: Date) = callbackFlow<MutableList<Phrase>> {
        phrasesCollection.addSnapshotListener { data, error ->
            if (error != null) {
                    channel.close(error)
            } else {
                if (data != null) {
                    channel.trySendBlocking(data.toObjects(Phrase::class.java))
                } else {
                    channel.close(Error("A general Error"))
                }
            }
        }
    }

}