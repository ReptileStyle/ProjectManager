package com.example.projectmanager.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.ui.data.DataViewModel
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import dev.bandb.graphview.AbstractGraphAdapter

class GraphAdapter(val viewModel: DataViewModel,val parent:HomeFragment): AbstractGraphAdapter<GraphAdapter.NodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.graph_item, parent, false)
        return NodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        Log.d("GraphAdapter",getNodeData(position).toString())
        holder.textView.text = getNodeData(position).toString()

        holder.box.setOnTouchListener { view, motionEvent ->
            var startPositionX:Float =viewModel.myMovements[position].x
            var startPositionY:Float= viewModel.myMovements[position].y
            var endPositionX:Float
            var endPositionY:Float
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN->{
                    startPositionX=motionEvent.x
                    startPositionY=motionEvent.y
                    Log.d("click","clicked - $startPositionX,$startPositionY")
                }
                MotionEvent.ACTION_MOVE->{
                    val currentX=motionEvent.x
                    val currentY=motionEvent.y
                    Log.d("click","start - ${startPositionX},${startPositionY}")
                    Log.d("click","moving - ${currentX},${currentY}")
//                    viewModel.myMovements[position].x=startPositionX+currentX
//                    viewModel.myMovements[position].y=startPositionY+currentY
//                    parent.setup()
                }
                MotionEvent.ACTION_UP->{
                    endPositionX=motionEvent.x
                    endPositionY=motionEvent.y
                   // Log.d("click","holder - ${holder.posX},${holder.posY}")
                    Log.d("click","viewModel - ${viewModel.myGraph.graph.nodes[position].moveX},${viewModel.myGraph.graph.nodes[position].moveX}")
                    Log.d("click","clicked - ${endPositionX - startPositionX},${endPositionY - startPositionY}")
                    viewModel.myMovements[position].x+=endPositionX
                    viewModel.myMovements[position].y+=endPositionY
                    parent.setup()
                }
            }//доделать на движение
            true
        }
    }
    class NodeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textView:TextView=itemView.findViewById(R.id.graph_node_text)
        val box:LinearLayout=itemView.findViewById(R.id.graph_node_box)
    }
}
