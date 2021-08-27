package com.radofigura.lottery.check.sk.fragments.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.fragments.listview.SubmittedPhrasesAdapter
import com.radofigura.lottery.check.sk.model.Phrase
import com.radofigura.lottery.check.sk.util.DateUtils
import com.radofigura.lottery.check.sk.util.PhraseFilteringManager
import com.radofigura.lottery.check.sk.viewmodel.CheckFullPhraseViewModel
import kotlinx.android.synthetic.main.fragment_check_full_phrase.*
import kotlinx.coroutines.launch
import java.util.*

class CheckFullPhrase : Fragment() {

    private val viewModel: CheckFullPhraseViewModel by hiltNavGraphViewModels(R.id.navigation_graph)
    lateinit var phrases: MutableList<Phrase>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
            : View? {

        return inflater.inflate(R.layout.fragment_check_full_phrase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            viewModel.currentPhasesBasedOnTimeLiveData.observe(viewLifecycleOwner, {
                if (it  != null) {
                    phrases = it
                    updatePhrases()
                } else {
                    fragment_check_full_phrase_submitted_phrases_loading.show()
                }
            })
        }
    }

    fun updateCurrentPhrase() {
        val phrase = PhraseFilteringManager.filterPhrase(phrases)
        if (phrase != null) {
            fragment_check_full_phrase_current_phrase.text = phrase.phrase
        } else {
            fragment_check_full_phrase_current_phrase.text = getString(R.string.string_current_password_placeholder)
        }
    }

    fun initSubmittedPhrases() {
        fragment_check_full_phrase_submitted_phrases.apply {
            adapter = SubmittedPhrasesAdapter(phrases)
        }
    }

    fun updatePhrases() {
        fragment_check_full_phrase_submitted_phrases_loading.hide()

        updateCurrentPhrase()

        if (fragment_check_full_phrase_submitted_phrases.adapter == null || fragment_check_full_phrase_submitted_phrases.adapter !is SubmittedPhrasesAdapter) {
            initSubmittedPhrases()
        }

        val lotteryAdapterToUpdate = fragment_check_full_phrase_submitted_phrases.adapter as SubmittedPhrasesAdapter

        fragment_check_full_phrase_submitted_phrases.apply {
            lotteryAdapterToUpdate.updatePhrases(phrases)
        }

        var oldestPhrase = PhraseFilteringManager.getNewestPhrase(phrases)

        fragment_check_full_phrase_phrases_lifespan.text = "${getString(R.string.string_phrase_list_age)} ${
            if (oldestPhrase == null)
                0
            else {
                DateUtils.getTimeInMinutesAndSeconds((Calendar.getInstance().time.time - oldestPhrase.dateAdded.time))
            }
        }"

        fragment_check_full_phrase_phrases_count.text = phrases.size.toString()

        if (phrases.isNullOrEmpty()) {
            fragment_check_full_phrase_no_phrases.visibility = View.VISIBLE
        } else {
            fragment_check_full_phrase_no_phrases.visibility = View.GONE
        }
    }
}