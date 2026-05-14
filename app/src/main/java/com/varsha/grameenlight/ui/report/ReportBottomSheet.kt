package com.varsha.grameenlight.ui.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.varsha.grameenlight.databinding.BottomSheetReportBinding
import com.varsha.grameenlight.ui.viewmodel.PoleViewModel

class ReportBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PoleViewModel by activityViewModels()

    companion object {
        fun newInstance(poleId: String) =
            ReportBottomSheet().apply {
                arguments = Bundle().apply {
                    putString("poleId", poleId)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetReportBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val poleId = arguments?.getString("poleId") ?: return

        binding.tvPoleId.text = poleId

        viewModel.poles.observe(viewLifecycleOwner) { poles ->
            val pole = poles.find { it.poleId == poleId }
            if (pole != null) {
                val statusText = when (pole.status) {
                    "WORKING" -> "Currently: Working Fine"
                    "FUSED"   -> "Currently: Bulb Fused"
                    "DAY_ON"  -> "Currently: Burning in Daytime"
                    else      -> "Status: ${pole.status}"
                }
                val statusColor = when (pole.status) {
                    "WORKING" -> Color.parseColor("#4CAF50")
                    "FUSED"   -> Color.parseColor("#F44336")
                    "DAY_ON"  -> Color.parseColor("#FF9800")
                    else      -> Color.WHITE
                }
                binding.tvPoleId.text =
                    "$poleId\n$statusText"
                binding.tvPoleId.setTextColor(statusColor)
            }
        }

        binding.btnWorking.setOnClickListener {
            submitReport(poleId, "WORKING")
        }
        binding.btnFused.setOnClickListener {
            submitReport(poleId, "FUSED")
        }
        binding.btnDayOn.setOnClickListener {
            submitReport(poleId, "DAY_ON")
        }
    }

    private fun submitReport(poleId: String, status: String) {
        viewModel.submitReport(poleId, status)

        val msg = when (status) {
            "WORKING" -> "Marked as Working — Map updated!"
            "FUSED"   -> "Complaint filed — Bulb Fused!"
            "DAY_ON"  -> "Complaint filed — Daytime Wastage!"
            else      -> "Report submitted!"
        }

        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_LONG
        ).show()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}