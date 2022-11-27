package com.example.projectmanager.ui.home

import android.graphics.Color
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.databinding.FragmentHomeBinding
import com.example.projectmanager.ui.data.DataViewModel
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.renameme.toStr
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.decoration.edge.ArrowDecoration
import dev.bandb.graphview.decoration.edge.StraightEdgeDecoration
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.energy.FruchtermanReingoldLayoutManager
import dev.bandb.graphview.layouts.layered.SugiyamaArrowEdgeDecoration
import dev.bandb.graphview.layouts.layered.SugiyamaLayoutManager
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment","created")
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setup()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setup(){
        val recycler = binding.recycler
        val configuration = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(100)
            .setSubtreeSeparation(100)
            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()
       // recycler.layoutManager = BuchheimWalkerLayoutManager(requireContext(), configuration)
        recycler.layoutManager= SugiyamaLayoutManager(requireContext()).apply {
            useMaxSize = true
        }

        val edgeStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 5f
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            pathEffect = CornerPathEffect(5f)
        }

        // 2. Attach item decorations to draw edges
        recycler.addItemDecoration(SugiyamaArrowEdgeDecoration(edgeStyle))



        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val dataViewModel =activity?.viewModels<DataViewModel>()!!.value
        Log.d("HomeFragment","graph-works = ${dataViewModel.myGraph.myEdges.size}")
        val adapter = GraphAdapter()
        adapter.apply {
            // 4.3 Submit the graph
            this.submitGraph( dataViewModel.myGraph.graph)
            recycler.adapter = this
        }
        dataViewModel.myGraph.myNodes.forEach {
            Log.d("HomeFrag","${it.works.toStr()}")
        }
    }

    override fun onResume() {
        Log.d("HomeFragment","resumed")
        super.onResume()
    }
}