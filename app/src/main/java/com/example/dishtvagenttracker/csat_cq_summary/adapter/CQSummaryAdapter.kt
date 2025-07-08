package com.example.dishtvagenttracker.csat_cq_summary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.model.CQSummary

class CQSummaryAdapter(private val cqSummaries: List<CQSummary>) :
    RecyclerView.Adapter<CQSummaryAdapter.CQSummaryViewHolder>() {

    class CQSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthYearTextView: TextView = itemView.findViewById(R.id.monthYearTextView)
        val averagePercentageTextView: TextView = itemView.findViewById(R.id.averagePercentageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CQSummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cq_summary, parent, false)
        return CQSummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CQSummaryViewHolder, position: Int) {
        val summary = cqSummaries[position]

        holder.monthYearTextView.text = summary.formattedMonthYear
        holder.averagePercentageTextView.text = String.format("Average Percentage: %.2f%%", summary.averagePercentage)
    }

    override fun getItemCount(): Int {
        return cqSummaries.size
    }
}