package com.radofigura.lottery.check.sk.service.actions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.radofigura.lottery.check.sk.model.Phrase
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GetPhraseBasedOnTime @Inject constructor(
    private val phrasesCollection: CollectionReference
) {

    suspend operator fun invoke(date: Date): ArrayList<Phrase> {
        return try {
            val documents = ArrayList<Phrase>()

            phrasesCollection
                .whereGreaterThanOrEqualTo("dateAdded", date)
                .orderBy("dateAdded", Query.Direction.DESCENDING)
                .get().await().documents.forEach {
                    documents.add(it.toObject<Phrase>()!!)
                }

            invke@documents
        } catch (e: Exception) {
            throw e;
        }
    }

}