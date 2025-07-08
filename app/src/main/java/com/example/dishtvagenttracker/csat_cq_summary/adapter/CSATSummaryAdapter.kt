package com.example.dishtvagenttracker.csat_cq_summary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.model.CSATSummary

class CSATSummaryAdapter(private val csatSummaries: List<CSATSummary>, private val onEditClick: (Int, Int) -> Unit) :
    RecyclerView.Adapter<CSATSummaryAdapter.CSATSummaryViewHolder>() {

    class CSATSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthYearTextView: TextView = itemView.findViewById(R.id.monthYearTextView)
        val totalT2CountTextView: TextView = itemView.findViewById(R.id.totalT2CountTextView)
        val totalB2CountTextView: TextView = itemView.findViewById(R.id.totalB2CountTextView)
        val totalNCountTextView: TextView = itemView.findViewById(R.id.totalNCountTextView)
        val csatPercentageTextView: TextView = itemView.findViewById(R.id.csatPercentageTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CSATSummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_csat_summary, parent, false)
        return CSATSummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CSATSummaryViewHolder, position: Int) {
        val summary = csatSummaries[position]

        holder.monthYearTextView.text = summary.formattedMonthYear
        holder.totalT2CountTextView.text = "Total T2 Count: ${summary.totalT2Count}"
        holder.totalB2CountTextView.text = "Total B2 Count: ${summary.totalB2Count}"
        holder.totalNCountTextView.text = "Total N Count: ${summary.totalNCount}"
        holder.csatPercentageTextView.text = String.format("CSAT Percentage: %.2f%%", summary.monthlyCSATPercentage)

        holder.editButton.setOnClickListener {
            onEditClick(summary.month, summary.year)
        }
    }

    override fun getItemCount(): Int {
        return csatSummaries.size
    }
}