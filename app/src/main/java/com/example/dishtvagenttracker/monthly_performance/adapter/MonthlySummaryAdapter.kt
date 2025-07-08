package com.example.dishtvagenttracker.monthly_performance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.model.MonthlySummary

class MonthlySummaryAdapter(private val monthlySummaries: List<MonthlySummary>) :
    RecyclerView.Adapter<MonthlySummaryAdapter.MonthlySummaryViewHolder>() {

    class MonthlySummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthYearTextView: TextView = itemView.findViewById(R.id.monthYearTextView)
        val totalLoginHoursTextView: TextView = itemView.findViewById(R.id.totalLoginHoursTextView)
        val totalCallsTextView: TextView = itemView.findViewById(R.id.totalCallsTextView)
        val baseSalaryTextView: TextView = itemView.findViewById(R.id.baseSalaryTextView)
        val bonusAmountTextView: TextView = itemView.findViewById(R.id.bonusAmountTextView)
        val csatBonusTextView: TextView = itemView.findViewById(R.id.csatBonusTextView)
        val grossSalaryTextView: TextView = itemView.findViewById(R.id.grossSalaryTextView)
        val tdsDeductionTextView: TextView = itemView.findViewById(R.id.tdsDeductionTextView)
        val netSalaryTextView: TextView = itemView.findViewById(R.id.netSalaryTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlySummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_monthly_summary, parent, false)
        return MonthlySummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthlySummaryViewHolder, position: Int) {
        val summary = monthlySummaries[position]

        holder.monthYearTextView.text = summary.formattedMonthYear
        holder.totalLoginHoursTextView.text = String.format("Total Login Hours: %.2f", summary.totalLoginHours)
        holder.totalCallsTextView.text = "Total Calls: ${summary.totalCalls}"
        holder.baseSalaryTextView.text = String.format("Base Salary: ₹%.2f", summary.baseSalary)
        holder.bonusAmountTextView.text = String.format("Bonus Amount: ₹%.2f", summary.bonusAmount)
        holder.csatBonusTextView.text = String.format("CSAT Bonus: ₹%.2f", summary.csatBonus)
        holder.grossSalaryTextView.text = String.format("Gross Salary: ₹%.2f", summary.grossSalary)
        holder.tdsDeductionTextView.text = String.format("TDS Deduction: ₹%.2f", summary.tdsDeduction)
        holder.netSalaryTextView.text = String.format("Net Salary: ₹%.2f", summary.netSalary)
    }

    override fun getItemCount(): Int {
        return monthlySummaries.size
    }
}