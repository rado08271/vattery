package com.radofigura.lottery.check.sk.fragments.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.util.CacheManager
import com.radofigura.lottery.check.sk.util.DateUtils
import com.radofigura.lottery.check.sk.viewmodel.UpdatePhraseViewModel
import kotlinx.android.synthetic.main.fragment_update_phrase.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class UpdatePhrase : Fragment() {
    private val viewModel: UpdatePhraseViewModel by hiltNavGraphViewModels(R.id.navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
            : View? {
        return inflater.inflate(R.layout.fragment_update_phrase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        randomHint()
        fragment_update_phrase_loading_bar.hide()

        lifecycleScope.launch {
            updateTimer()
                .collect {
                    fragment_update_phrase_current_time.text = it
                }
        }

        fragment_update_phrase_phrase_input.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEND) {
                createNewPhrase(textView.text.toString())
            }


            true
        }

        fragment_update_phrase_phrase_send.setOnClickListener {
                createNewPhrase(fragment_update_phrase_phrase_input.text!!.toString())
        }
    }

    fun updateTimer() = flow {
        while (true) {
            emit(DateUtils.getCurrentFormattedTime())
            delay(1000)
        }
    }

    fun randomHint() {
        fragment_update_phrase_phrase_input.hint = listOf<String>(
            "Sulik nenakupil testy",
            "Posrata kompe",
            "Macka vo vreci",
            "Chskym",
            "Ja si to uzivam ja sa citim dobre",
            "Hyckanie a opateru",
            "Historik",
            "One time next time",
            "Bromhexin",
            "Anca",
            "Drz balonik",
            "Gagaji, taraji, prdusi",
            "Skutok sa nestal",
            "Tupy chj",
            "Trtosit sa",
            "Diablov syn",
            "Vlastnou hlavou",
            "Pekny biely den",
            "Protislovenské prostitútky",
            "Ludia otupjevjaju",
            "Polka sem polka sem polka sem",
            "Na vychode nic nie je",
            "Volic je uplne hovno",
            "Bude to drahe",
            "Zatva",
            "Pocem",
            "Salarabanda",
        ).random()
    }

    fun createNewPhrase(text: String) {
        if (!checkInput(text)) {
            fragment_update_phrase_phrase_input.error = getString(R.string.string_password_numbers_empty_error)
            return
        }

        lifecycleScope.launch {
            delay(500)
            fragment_update_phrase_phrase_input.setText("")

            viewModel.createNewPhrase(text, CacheManager.getString(getString(R.string.cache_user_generated_id), null, requireContext()))
                .collect {
                    when (it) {
                        is State.LoadingState -> {
                            Toast.makeText(requireContext(), getString(R.string.string_prase_sent_succesfully_toast), Toast.LENGTH_LONG).show()
                            fragment_update_phrase_loading_bar.show()
                        }
                        is State.DataState -> fragment_update_phrase_loading_bar.hide()
                        is State.ErrorState -> {
                            fragment_update_phrase_loading_bar.hide()
                        }
                    }
                }
        }
    }

    fun checkInput(text: String) = !text.isNullOrBlank() && !text.contains(Regex("[0-9]"));

}