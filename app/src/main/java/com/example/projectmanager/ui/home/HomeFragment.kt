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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.databinding.FragmentHomeBinding
import com.example.projectmanager.ui.data.GraphBuilder
import dev.bandb.graphview.AbstractGraphAdapter
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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
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
            pathEffect = CornerPathEffect(10f)
            //как уменьшить стрелочки?
        }


        // 2. Attach item decorations to draw edges
        recycler.addItemDecoration(SugiyamaArrowEdgeDecoration(edgeStyle))

        val myGraph = GraphBuilder()
        myGraph.createGraph()
//        Log.d("HomeFragment",myGraph.graph.toString())
//        val graph = Graph()
//        val node1 = Node("Parent")
//        val node2 = Node("Child 1")
//        val node3 = Node("Child 2")
//
//        graph.addEdge(node1, node2)
//        graph.addEdge(node1, node3)

        val adapter = GraphAdapter()
        adapter.apply {
            // 4.3 Submit the graph
            this.submitGraph(myGraph.graph)
            recycler.adapter = this
        }
    }
}