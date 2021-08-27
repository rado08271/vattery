package com.radofigura.lottery.check.sk.fragments.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.model.Phrase
import com.radofigura.lottery.check.sk.util.DateUtils
import kotlinx.android.synthetic.main.item_submitted_phrase.view.*

class SubmittedPhrasesAdapter(private var phrases: List<Phrase>): RecyclerView.Adapter<SubmittedPhrasesAdapter.SubmittedPhrasesViewHolder>() {

    fun updatePhrases(phrases: List<Phrase>) {
        this.phrases = phrases
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmittedPhrasesViewHolder {
        return SubmittedPhrasesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_submitted_phrase, parent, false))
    }

    override fun onBindViewHolder(holder: SubmittedPhrasesViewHolder, position: Int) {

        holder.senderId.text = phrases[position].senderId
        holder.senderTime.text = DateUtils.getTimeWithFormat(phrases[position].dateAdded,"dd.MMMM.yyyy HH:mm:ss")
        holder.senderPhrase.text = phrases[position].phrase

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, phrases[position].phrase, Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int = phrases.size

    inner class SubmittedPhrasesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val senderId = view.item_submitted_phrase_sender_id
        val senderTime = view.item_submitted_phrase_sender_time
        val senderPhrase = view.item_submitted_phrase_submitted_phrase

    }

}