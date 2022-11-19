package com.example.projectmanager.ui.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.databinding.FragmentDataBinding

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private val viewModel:DataViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataViewModel =
            ViewModelProvider(this).get(DataViewModel::class.java)

        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.dataRecyclerView
        val dataset = viewModel.getDataset()
        recyclerView.adapter = TableAdapter(dataset)
        recyclerView.layoutManager=LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
       // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}