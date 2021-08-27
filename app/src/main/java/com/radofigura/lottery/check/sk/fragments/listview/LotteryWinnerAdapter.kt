package com.radofigura.lottery.check.sk.fragments.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.model.Winner
import kotlinx.android.synthetic.main.item_lottery_winner.view.*
import java.text.NumberFormat
import java.util.*

class LotteryWinnerAdapter(private var winners: List<Winner>): RecyclerView.Adapter<LotteryWinnerAdapter.LotteryWinnerViewHolder>() {

    fun updateWinners(winners: List<Winner>) {
        this.winners = winners
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotteryWinnerViewHolder {
        return LotteryWinnerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lottery_winner, parent, false))
    }

    override fun onBindViewHolder(holder: LotteryWinnerViewHolder, position: Int) {
        holder.lotteryWinnerName.text = winners[position].firstName
        holder.lotteryWinnerCity.text = winners[position].city
        holder.lotteryWinnerTicket.text = winners[position].ticketId

        val prizeFormat = NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance("EUR")
            maximumFractionDigits = 0
        }

        holder.lotteryWinnerPrize.text = prizeFormat.format(winners[position].winningPrize.toInt())

//        holder.itemView.setOnClickListener {
//            Toast.makeText(holder.itemView.context, "Dakujeme ${winners[position].firstName} za zaockovanie, vela zdravia!", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun getItemCount(): Int {
        return winners.size
    }

    inner class LotteryWinnerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val lotteryWinnerName = view.item_lottery_winner_winner_name
        val lotteryWinnerCity = view.item_lottery_winner_winner_city
        val lotteryWinnerTicket = view.item_lottery_winner_winner_lottery_ticket
        val lotteryWinnerPrize = view.item_lottery_winner_winner_prize
    }
}