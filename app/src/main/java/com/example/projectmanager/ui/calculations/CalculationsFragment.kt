package com.example.projectmanager.ui.calculations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.databinding.FragmentCalculationsBinding

import com.example.projectmanager.databinding.WorksTableBasicBinding
import com.example.projectmanager.ui.data.DataViewModel

class CalculationsFragment : Fragment() {

    private var _binding:FragmentCalculationsBinding? = null
    private val viewModel: CalculationsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val dataViewModel =
            activity?.viewModels<DataViewModel>()!!.value

        _binding = FragmentCalculationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.calculationsRecyclerView
        val dataset = viewModel.getDataset()
        recyclerView.adapter = SuperAdapter(dataViewModel,activity,recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(activity, RecyclerView.VERTICAL,false)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}