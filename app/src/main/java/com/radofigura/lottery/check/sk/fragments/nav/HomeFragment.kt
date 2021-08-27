package com.radofigura.lottery.check.sk.fragments.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.fragments.listview.UpdateLotteryTicketAdapter
import com.radofigura.lottery.check.sk.model.Ticket
import com.radofigura.lottery.check.sk.util.DateUtils
import com.radofigura.lottery.check.sk.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.navigation_graph)
    lateinit var tickets: MutableList<Ticket>

    var lifecycleRunning = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
            : View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenResumed {
            viewModel.currentPhaseBasedOnTimeLiveData.observe(viewLifecycleOwner, {
                if (it != null) {
                    if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                        // FIXME: coroutines is not finished and you attempt to update unbound element
                        fragment_home_current_phrase.text = it.phrase
                        fragment_home_current_phrase_time_added_last.text =
                            DateUtils.getFormattedTime(it.dateAdded)
                    }
                } else {
                    fragment_home_current_phrase.text = getString(R.string.string_current_password_placeholder)
                    fragment_home_current_phrase_time_added_last.text =
                        DateUtils.getCurrentFormattedTime()
                }
            })

            viewModel.allLotteryTicketsLiveData.observe(viewLifecycleOwner, {
                if (it != null) {
                    tickets = it
                    updateTicketsBasedOnLotteryWinners()
                } else {
                    fragment_home_my_current_lottery_progress.hide()
                }
            })
        }

        fragment_home_check_for_lottery_winners.setOnClickListener {
            lifecycleRunning = false;

            findNavController().navigate(
                R.id.action_homeFragment_to_checkWinners,
                null,
                navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                    this.launchSingleTop = true
                    this.restoreState = true
                }
            )
        }

        fragment_home_add_new_phrase_card.setOnClickListener {
            lifecycleRunning = false;

            findNavController().navigate(
                R.id.action_homeFragment_to_updatePhrase,
                null,
                navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                    this.launchSingleTop = true
                    this.restoreState = true
                }
            )

        }

        fragment_home_current_phrase_view.setOnClickListener {
            lifecycleRunning = false;

            findNavController().navigate(
                R.id.action_homeFragment_to_checkFullPhrase,
                null,
                navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                    this.launchSingleTop = true
                    this.restoreState = true
                }
            )
        }

        fragment_home_add_my_lottery_tickets.setOnClickListener {
            lifecycleRunning = false;

            findNavController().navigate(
                R.id.action_homeFragment_to_addLotteryTickets,
                null,
                navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                    this.launchSingleTop = true
                    this.restoreState = true
                }
            )

        }
    }

    fun updateMyTickets() {
        fragment_home_my_current_lottery_progress.hide()
        fragment_home_my_current_lottery_tickets.adapter = UpdateLotteryTicketAdapter(tickets)
    }

    fun updateTicketsBasedOnLotteryWinners() {
        lifecycleScope.launchWhenStarted {
            viewModel.filterLotteryWinners(tickets)
            viewModel.filteredWinningTicketsViewMode.observe(viewLifecycleOwner, {

                if (it != null) {
                    tickets = it
                }

                updateMyTickets()
            })
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.shouldContinue = false
    }

    override fun onResume() {
        super.onResume()
        viewModel.init()
    }
}