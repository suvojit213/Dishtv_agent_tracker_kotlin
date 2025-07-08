package com.example.dishtvagenttracker.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.model.DailyEntry
import java.text.SimpleDateFormat
import java.util.Locale

class DailyEntryAdapter(private val dailyEntries: List<DailyEntry>, private val onEditClick: (DailyEntry) -> Unit) :
    RecyclerView.Adapter<DailyEntryAdapter.DailyEntryViewHolder>() {

    class DailyEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val loginTimeTextView: TextView = itemView.findViewById(R.id.loginTimeTextView)
        val callCountTextView: TextView = itemView.findViewById(R.id.callCountTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyEntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_entry, parent, false)
        return DailyEntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyEntryViewHolder, position: Int) {
        val dailyEntry = dailyEntries[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        holder.dateTextView.text = "Date: ${dateFormat.format(dailyEntry.date)}"
        holder.loginTimeTextView.text = String.format(
            Locale.getDefault(),
            "Login Time: %02d:%02d:%02d",
            dailyEntry.loginHours,
            dailyEntry.loginMinutes,
            dailyEntry.loginSeconds
        )
        holder.callCountTextView.text = "Call Count: ${dailyEntry.callCount}"

        holder.editButton.setOnClickListener {
            onEditClick(dailyEntry)
        }
    }

    override fun getItemCount(): Int {
        return dailyEntries.size
    }
}