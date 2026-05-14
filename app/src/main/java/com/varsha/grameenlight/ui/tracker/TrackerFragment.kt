package com.varsha.grameenlight.ui.tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.varsha.grameenlight.databinding.FragmentTrackerBinding
import com.varsha.grameenlight.ui.viewmodel.PoleViewModel

class TrackerFragment : Fragment() {
    private var _binding: FragmentTrackerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PoleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackerBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ReportAdapter()
        binding.rvReports.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rvReports.adapter = adapter

        viewModel.reports.observe(viewLifecycleOwner) { reports ->
            adapter.submitList(reports)
            binding.tvCount.text =
                "${reports.size} complaints"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}