package com.example.cowinnotifier.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cowinnotifier.R
import com.example.cowinnotifier.model.Session
import com.example.cowinnotifier.utils.GeneralUtil
import kotlinx.android.synthetic.main.row_session.view.*

class SessionAdapter(private val sessionList: List<Session>) :
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_session, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val eachSession = sessionList[position]

        holder.tvDate.text = GeneralUtil.getDateFromDateString(eachSession.date)
        holder.tvMonth.text = GeneralUtil.getMonthFromDateString(eachSession.date)

        holder.tvDose.text = "1st" // TODO update as per dose input
        holder.tvVaccine.text = eachSession.vaccine
        holder.tvSlots.text = eachSession.available_capacity.toString()
        holder.tvAge.text = eachSession.min_age_limit.toString()
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate = itemView.tvDate
        val tvMonth = itemView.tvMonth
        val tvDose = itemView.tvDose
        val tvVaccine = itemView.tvVaccine
        val tvAge = itemView.tvAge
        val tvSlots = itemView.tvSlots
    }
}