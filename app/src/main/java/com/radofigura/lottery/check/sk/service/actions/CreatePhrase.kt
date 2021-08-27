package com.radofigura.lottery.check.sk.service.actions

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.radofigura.lottery.check.sk.model.Phrase
import com.radofigura.lottery.check.sk.model.Winner
import com.radofigura.lottery.check.sk.service.api.MfApi
import java.lang.Exception
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import javax.inject.Inject

class CreatePhrase @Inject constructor(
    private val phrasesCollection: CollectionReference
) {

    suspend operator fun invoke(phrase: Phrase): Boolean {
        return try {
            phrasesCollection.add(phrase).await()
            true
        } catch (e: Exception) {
            throw e
        }
    }

}