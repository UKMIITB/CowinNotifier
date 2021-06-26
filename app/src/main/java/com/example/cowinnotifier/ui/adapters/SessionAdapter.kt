package com.example.cowinnotifier.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Session
import com.example.cowinnotifier.utils.GeneralUtil
import kotlinx.android.synthetic.main.row_session.view.*

class SessionAdapter(private val sessionList: List<Session>, private val bundle: Bundle) :
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_session, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val eachSession = sessionList[position]
        val slots = if (AppConstants.STRING_TO_DOSE_MAP[bundle.getString(AppConstants.DOSE, "")].equals("1st"))
            eachSession.available_capacity_dose1
        else
            eachSession.available_capacity_dose2

        val context = holder.sessionMain.context

        holder.tvDate.text = GeneralUtil.getDateFromDateString(eachSession.date)
        holder.tvMonth.text = GeneralUtil.getMonthFromDateString(eachSession.date)

        holder.tvDose.text = AppConstants.STRING_TO_DOSE_MAP[bundle.getString(AppConstants.DOSE, "")]
        holder.tvVaccine.text = eachSession.vaccine
        holder.tvSlots.text = slots.toString()
        holder.tvAge.text = eachSession.min_age_limit.toString()

        when {
            slots < 10 -> holder.sessionMain.setBackgroundColor(ContextCompat.getColor(context, R.color.capacity_low))
            slots < 20 -> holder.sessionMain.setBackgroundColor(ContextCompat.getColor(context, R.color.capacity_medium))
            else -> holder.sessionMain.setBackgroundColor(ContextCompat.getColor(context, R.color.capacity_high))
        }
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
        val sessionMain = itemView.session_main
    }
}