package com.example.dishtvagenttracker.cq_entries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.model.CQEntry
import java.text.SimpleDateFormat
import java.util.Locale

class CQEntryListAdapter(private val cqEntries: List<CQEntry>, private val onEditClick: (CQEntry) -> Unit) :
    RecyclerView.Adapter<CQEntryListAdapter.CQEntryViewHolder>() {

    class CQEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val auditDateTextView: TextView = itemView.findViewById(R.id.auditDateTextView)
        val percentageTextView: TextView = itemView.findViewById(R.id.percentageTextView)
        val qualityRatingTextView: TextView = itemView.findViewById(R.id.qualityRatingTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CQEntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cq_entry_list, parent, false)
        return CQEntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CQEntryViewHolder, position: Int) {
        val cqEntry = cqEntries[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        holder.auditDateTextView.text = "Audit Date: ${dateFormat.format(cqEntry.auditDate)}"
        holder.percentageTextView.text = String.format("Percentage: %.2f%%", cqEntry.percentage)
        holder.qualityRatingTextView.text = "Rating: ${cqEntry.qualityRating}"

        holder.editButton.setOnClickListener {
            onEditClick(cqEntry)
        }
    }

    override fun getItemCount(): Int {
        return cqEntries.size
    }
}