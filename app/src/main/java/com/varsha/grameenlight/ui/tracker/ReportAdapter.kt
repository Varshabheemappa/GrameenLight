package com.varsha.grameenlight.ui.tracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varsha.grameenlight.R
import com.varsha.grameenlight.data.local.ReportEntity
import com.varsha.grameenlight.databinding.ItemReportBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportAdapter : ListAdapter<ReportEntity,
        ReportAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(
        private val binding: ItemReportBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(report: ReportEntity) {
            binding.tvComplaintId.text = report.complaintId
            binding.tvPoleId.text = report.poleId
            binding.tvReportedStatus.text =
                "Status: ${report.reportedStatus}"

            val date = SimpleDateFormat(
                "dd MMM yyyy, hh:mm a", Locale.getDefault()
            ).format(Date(report.reportedAt))
            binding.tvReportedAt.text = "Reported: $date"

            binding.tvRepairStatus.text = report.repairStatus

            when (report.repairStatus) {
                "PENDING" -> {
                    binding.tvRepairStatus
                        .setBackgroundColor(Color.parseColor("#E65100"))
                }
                "ASSIGNED" -> {
                    binding.tvRepairStatus
                        .setBackgroundColor(Color.parseColor("#1565C0"))
                }
                "FIXED" -> {
                    binding.tvRepairStatus
                        .setBackgroundColor(Color.parseColor("#2E7D32"))
                }
            }

            when (report.reportedStatus) {
                "WORKING" -> binding.statusDot
                    .setBackgroundResource(R.drawable.bg_dot_green)
                "FUSED" -> binding.statusDot
                    .setBackgroundResource(R.drawable.bg_dot_red)
                "DAY_ON" -> binding.statusDot
                    .setBackgroundResource(R.drawable.bg_dot_orange)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<ReportEntity>() {
        override fun areItemsTheSame(
            a: ReportEntity,
            b: ReportEntity
        ) = a.complaintId == b.complaintId

        override fun areContentsTheSame(
            a: ReportEntity,
            b: ReportEntity
        ) = a == b
    }
}