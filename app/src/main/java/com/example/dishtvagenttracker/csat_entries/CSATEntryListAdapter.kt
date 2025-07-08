package com.example.dishtvagenttracker.csat_entries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.model.CSATEntry
import java.text.SimpleDateFormat
import java.util.Locale

class CSATEntryListAdapter(private val csatEntries: List<CSATEntry>, private val onEditClick: (CSATEntry) -> Unit) :
    RecyclerView.Adapter<CSATEntryListAdapter.CSATEntryViewHolder>() {

    class CSATEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val t2CountTextView: TextView = itemView.findViewById(R.id.t2CountTextView)
        val b2CountTextView: TextView = itemView.findViewById(R.id.b2CountTextView)
        val nCountTextView: TextView = itemView.findViewById(R.id.nCountTextView)
        val csatPercentageTextView: TextView = itemView.findViewById(R.id.csatPercentageTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CSATEntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_csat_entry_list, parent, false)
        return CSATEntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CSATEntryViewHolder, position: Int) {
        val csatEntry = csatEntries[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        holder.dateTextView.text = "Date: ${dateFormat.format(csatEntry.date)}"
        holder.t2CountTextView.text = "T2 Count: ${csatEntry.t2Count}"
        holder.b2CountTextView.text = "B2 Count: ${csatEntry.b2Count}"
        holder.nCountTextView.text = "N Count: ${csatEntry.nCount}"
        holder.csatPercentageTextView.text = String.format("CSAT %%: %.2f%%", csatEntry.csatPercentage)

        holder.editButton.setOnClickListener {
            onEditClick(csatEntry)
        }
    }

    override fun getItemCount(): Int {
        return csatEntries.size
    }
}