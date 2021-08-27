package com.radofigura.lottery.check.sk.fragments.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.model.Ticket
import kotlinx.android.synthetic.main.item_lotter_ticket_update.view.*

class UpdateLotteryTicketAdapter(private var tickets: List<Ticket>, private val onClickDelete: ((ticket: Ticket) -> Unit)? = null): RecyclerView.Adapter<UpdateLotteryTicketAdapter.UpdateLotteryTicketViewHolder>() {
    fun updateTickets(tickets: List<Ticket>) {
        this.tickets = tickets
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpdateLotteryTicketViewHolder {
        return UpdateLotteryTicketViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lotter_ticket_update, parent, false))
    }

    override fun onBindViewHolder(holder: UpdateLotteryTicketViewHolder, position: Int) {
        holder.itemID.text = (position + 1).toString()
        holder.itemCode.text = tickets[position].ticketId
        holder.itemStatus.text =
            if (tickets[position].won) holder.itemView.context.getString(R.string.string_my_ticket_won_status)
            else holder.itemView.context.getString(R.string.string_my_ticket_active_status)

        if (tickets[position].won) {
            holder.winningCard.visibility = View.VISIBLE
            holder.winnigPrize.text = tickets[position].prize.toString()
        }


        if (onClickDelete != null) {
            holder.delete.setOnClickListener {
                onClickDelete.invoke(tickets[position])
            }
        } else {
            holder.delete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = tickets.size

    inner class UpdateLotteryTicketViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val itemID = view.item_lottery_ticket_update_ticket_title
        val itemCode = view.item_lottery_ticket_update_ticket_code
        val itemStatus = view.item_lottery_ticket_update_ticket_status
        val delete = view.item_lottery_ticket_update_ticket_delete
        val winnigPrize = view.item_lottery_ticket_update_ticket_prize
        val winningCard = view.item_lottery_ticket_update_ticket_card
    }

}