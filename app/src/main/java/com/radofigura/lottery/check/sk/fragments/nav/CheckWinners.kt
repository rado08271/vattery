package com.radofigura.lottery.check.sk.fragments.nav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.fragments.listview.LotteryWinnerAdapter
import com.radofigura.lottery.check.sk.model.Winner
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.viewmodel.CheckWinnersViewModel
import kotlinx.android.synthetic.main.fragment_check_winners.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckWinners : Fragment() {

    private val viewModel: CheckWinnersViewModel by hiltNavGraphViewModels(R.id.navigation_graph)
    lateinit var winners: ArrayList<Winner>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
            : View? {

        return inflater.inflate(R.layout.fragment_check_winners, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(500)

            viewModel.getAllLotteryWinners()
                .collect {
                    when (it) {
                        is State.LoadingState -> fragment_check_winners_loading_bar.show()
                        is State.DataState -> {
                            winners = it.data
                            initializeLotteryWinnersList(it.data)
                        }
                        is State.ErrorState -> Log.i("ERROR", it.exception.message!!);
                    }
                }
        }
    }

    fun initializeLotteryWinnersList(winners: List<Winner>) {
        addSearchBar()

        fragment_check_winners_loading_bar.hide()
        fragment_check_winners_results_count.text = winners.size.toString();

        fragment_check_winners_winners_list.apply {
            adapter = LotteryWinnerAdapter(winners)
        }
    }

    fun updateLotteryList (winners: List<Winner>) {
        val lotteryAdapterToUpdate = fragment_check_winners_winners_list.adapter as LotteryWinnerAdapter

        fragment_check_winners_results_count.text = winners.size.toString();

        fragment_check_winners_winners_list.apply {
            lotteryAdapterToUpdate.updateWinners(winners)
        }

    }

    fun addSearchBar() {
        fragment_check_winners_search_bar.setOnQueryTextListener (object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null && query.length >= 3) {
                    lifecycleScope.launch {

                        viewModel.filterWinnersQuery(query!!, winners)
                            .collect {
                                updateLotteryList(it)
                            }
                    }
                }

                if ((query.isNullOrEmpty())) {
                    updateLotteryList(winners)
                }

                return true
            }
        })
    }
}