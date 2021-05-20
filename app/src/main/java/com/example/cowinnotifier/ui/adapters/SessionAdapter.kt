package com.example.cowinnotifier.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cowinnotifier.R
import com.example.cowinnotifier.model.Session
import kotlinx.android.synthetic.main.row_session.view.*

class SessionAdapter(private val sessionList: List<Session>) :
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_session, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val eachSession = sessionList[position]
        holder.textViewDate.text = eachSession.date

        val context = holder.textViewDoses.context

        if (eachSession.available_capacity < 10) {
            holder.textViewDoses.background.setTint(ContextCompat.getColor(context, R.color.capacity_less_than_10))
        } else if (eachSession.available_capacity < 20) {
            holder.textViewDoses.background.setTint(ContextCompat.getColor(context, R.color.capacity_less_than_20))
        } else {
            holder.textViewDoses.background.setTint(ContextCompat.getColor(context, R.color.capacity_less_than_30))
        }

        holder.textViewDoses.text = eachSession.available_capacity.toString()
        holder.textViewVaccineName.text = eachSession.vaccine

        val ageLimitText = "Age ${eachSession.min_age_limit}+"
        holder.textViewAgeLimit.text = ageLimitText
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDate = itemView.textview_date
        val textViewDoses = itemView.textview_doses
        val textViewVaccineName = itemView.textview_vaccine_name
        val textViewAgeLimit = itemView.textview_age_limit
    }
}