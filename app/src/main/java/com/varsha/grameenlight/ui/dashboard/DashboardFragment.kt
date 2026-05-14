package com.varsha.grameenlight.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.varsha.grameenlight.databinding.FragmentDashboardBinding
import com.varsha.grameenlight.ui.viewmodel.PoleViewModel

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PoleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.daytimeCount.observe(viewLifecycleOwner) { count ->
            binding.tvEnergySaved.text =
                "$count daytime burnouts reported this month"
            binding.progressEnergy.progress =
                (count * 10).coerceAtMost(100)
            val kwh = count * 2
            binding.tvKwh.text = "$kwh kWh"
        }

        viewModel.poles.observe(viewLifecycleOwner) { poles ->
            val workingCount = poles.count { it.status == "WORKING" }
            val fusedCount   = poles.count { it.status == "FUSED"   }
            val dayOnCount   = poles.count { it.status == "DAY_ON"  }

            binding.tvWorking.text      = "$workingCount"
            binding.tvFused.text        = "$fusedCount"
            binding.tvWorkingCount.text = "$workingCount"
            binding.tvFusedCount.text   = "$fusedCount"
            binding.tvDayOn.text        = "$dayOnCount"

            binding.progressWorking.progress = workingCount
            binding.progressFused.progress   = fusedCount
            binding.progressDayon.progress   = dayOnCount

            if (fusedCount == 0 && dayOnCount == 0) {
                binding.tvZeroDark.text = "All poles working!"
                binding.tvZeroDark.setTextColor(
                    resources.getColor(com.varsha.grameenlight.R.color.colorWorking, null)
                )
            } else {
                binding.tvZeroDark.text =
                    "$fusedCount fused · $dayOnCount wasting energy"
                binding.tvZeroDark.setTextColor(
                    resources.getColor(com.varsha.grameenlight.R.color.colorFused, null)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}