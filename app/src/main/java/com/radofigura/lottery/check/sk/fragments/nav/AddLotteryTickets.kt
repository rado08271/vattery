package com.radofigura.lottery.check.sk.fragments.nav

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.fragments.listview.UpdateLotteryTicketAdapter
import com.radofigura.lottery.check.sk.model.Ticket
import com.radofigura.lottery.check.sk.service.State
import com.radofigura.lottery.check.sk.util.StringUtils
import com.radofigura.lottery.check.sk.viewmodel.AddLotteryTicketsViewModel
import kotlinx.android.synthetic.main.fragment_add_lottery_tickets.*

class AddLotteryTickets : Fragment() {

    val viewModel: AddLotteryTicketsViewModel by hiltNavGraphViewModels(R.id.navigation_graph)
    lateinit var tickets: MutableList<Ticket>
    lateinit var winners: MutableList<Ticket>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        return inflater.inflate(R.layout.fragment_add_lottery_tickets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_add_lottery_ticket_add_button.setOnClickListener {
            val lotteryTicket = fragment_add_lottery_tickets_lottery_form.text

            if (validateInput(lotteryTicket)) {
                addNewTicket(lotteryTicket.toString())
            }
        }

        getLotteryTickets()

        viewModel.allLotteryTicketsLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                tickets = it

                val adapter = fragment_add_lottery_ticket_list_of_tickets.adapter
                if (adapter == null || (adapter != null && adapter !is UpdateLotteryTicketAdapter)) {
                    fragment_add_lottery_ticket_list_of_tickets.adapter =
                        UpdateLotteryTicketAdapter(tickets) { ticket ->
                            viewModel.removeLotteryTicket(ticket)
                        }
                } else if (adapter is UpdateLotteryTicketAdapter) {
                    adapter.updateTickets(tickets)
                }
            } else {
            }
        })

        viewModel.lotteryTicketRemoveResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it) {
                    is State.LoadingState -> { }
                    is State.ErrorState -> Log.e("ERROR", it.exception.message, it.exception)
                    is State.DataState -> getLotteryTickets()
                }
            }
        })

        viewModel.lotteryTicketResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it) {
                    is State.LoadingState -> { }
                    is State.ErrorState -> Log.e("ERROR", it.exception.message, it.exception)
                    is State.DataState -> getLotteryTickets()
                }
            }
        })
    }

    fun getLotteryTickets() {
        viewModel.getLotteryTickets()
    }

    fun addNewTicket(lotteryTicket: String) {

        lifecycleScope.launchWhenStarted {
            viewModel
                .addLotteryTickets(Ticket(ticketId = StringUtils.addHyphens(lotteryTicket)))
        }
    }

    fun validateInput(lotteryTicket: Editable?): Boolean {

        if (lotteryTicket.isNullOrEmpty()) {
            fragment_add_lottery_tickets_error_message.text = getString(R.string.string_empty_password_input_error)
            return false
        }

        if (!lotteryTicket.matches(Regex("\\A[\\w\\d]{12}"))) {
            fragment_add_lottery_tickets_error_message.text = getString(R.string.string_wrong_password_input_error)
            return false
        } else {
            fragment_add_lottery_tickets_error_message.text = ""
            return true
        }
    }
}