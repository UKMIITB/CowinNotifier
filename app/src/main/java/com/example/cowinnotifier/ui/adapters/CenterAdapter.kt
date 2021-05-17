package com.example.cowinnotifier.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cowinnotifier.R
import com.example.cowinnotifier.model.Center
import kotlinx.android.synthetic.main.row_center.view.*

class CenterAdapter(private val centerList: List<Center>) :
    RecyclerView.Adapter<CenterAdapter.CenterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_center, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val eachCenter = centerList[position]
        holder.textViewCenterName.text = eachCenter.name
        holder.textViewCenterAddress.text = eachCenter.address

        val layoutManager = LinearLayoutManager(
            holder.recyclerviewSessions.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        layoutManager.initialPrefetchItemCount = eachCenter.sessions.size

        val sessionAdapter = SessionAdapter(eachCenter.sessions)
        holder.recyclerviewSessions.layoutManager = layoutManager
        holder.recyclerviewSessions.adapter = sessionAdapter
    }

    override fun getItemCount(): Int {
        return centerList.size
    }

    inner class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCenterName = itemView.textview_center_name
        val textViewCenterAddress = itemView.textview_center_address
        val recyclerviewSessions = itemView.recyclerview_session_list
    }
}