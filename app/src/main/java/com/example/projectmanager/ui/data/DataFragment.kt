package com.example.projectmanager.ui.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.databinding.FragmentDataBinding

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    //private val viewModel:DataViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("datafragment","fragment on create")
        val viewModel = activity?.viewModels<DataViewModel>()!!.value
//        val dataViewModel =
//        ViewModelProvider(this).get(DataViewModel::class.java)

        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.dataRecyclerView
        val dataset = viewModel.getDataset()
        recyclerView.layoutManager=LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        binding.radioButtons.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                binding.radioButtonBasicMode.id->{
                    viewModel.changeMode(1)
                    recyclerView.adapter = TableAdapter(viewModel,1,requireActivity())
                    binding.dataHeaderRowBasic.visibility=View.VISIBLE
                    binding.dataHeaderRowAdvanced.visibility=View.GONE
                    binding.dataHeaderRowExpert.visibility=View.GONE
                }
                binding.radioButtonAdvancedMode.id->{
                    viewModel.changeMode(2)
                    recyclerView.adapter = TableAdapter(viewModel,2,requireActivity())
                    binding.dataHeaderRowBasic.visibility=View.GONE
                    binding.dataHeaderRowAdvanced.visibility=View.VISIBLE
                    binding.dataHeaderRowExpert.visibility=View.GONE
                }
                binding.radioButtonExpertMode.id->{
                    viewModel.changeMode(3)
                    recyclerView.adapter = TableAdapter(viewModel,3,requireActivity())
                    binding.dataHeaderRowBasic.visibility=View.GONE
                    binding.dataHeaderRowAdvanced.visibility=View.GONE
                    binding.dataHeaderRowExpert.visibility=View.VISIBLE
                }
            }
        }
        binding.buttonAddWork.setOnClickListener {
            DialogAddWorkFragment(viewModel).show(this.parentFragmentManager,"")
        }
        binding.radioButtonBasicMode.isChecked=true
       // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}