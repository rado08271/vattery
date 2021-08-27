package com.radofigura.lottery.check.sk.service.actions

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.radofigura.lottery.check.sk.model.Phrase
import javax.inject.Inject

class GetAllPhrases @Inject constructor(
    private val phrasesCollection: CollectionReference
) {

    suspend operator fun invoke(): Task<QuerySnapshot> {
        return phrasesCollection
            .get()
    }

}