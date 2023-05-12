package com.example.projectmanager.ui.optimization

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.ui.data.DataViewModel
import com.example.projectmanager.ui.home.HomeViewModel
import com.example.projectmanager.ui.renameme.Work
import com.kuzevanov.filemanager.ui.theme.MyApplicationTheme

class OptimizationFragment : Fragment() {
    private lateinit var optimizationViewModel: OptimizationViewModel
    private lateinit var workList: List<Work>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        optimizationViewModel = ViewModelProvider(this)[OptimizationViewModel::class.java]
        workList = activity?.viewModels<DataViewModel>()!!.value.getDataset()
        optimizationViewModel.setDataset(workList)
//        Log.d("asd",workList.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApplicationTheme() {
                    OptimizationComposeScreen(optimizationViewModel)
                }
            }
        }
    }
}