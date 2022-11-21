package com.example.projectmanager.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import dev.bandb.graphview.AbstractGraphAdapter

class GraphAdapter(): AbstractGraphAdapter<GraphAdapter.NodeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.graph_item, parent, false)
        return NodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        Log.d("GraphAdapter",getNodeData(position).toString())
        holder.textView.text = getNodeData(position).toString()
    }
    class NodeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textView:TextView=itemView.findViewById(R.id.graph_node_text)
    }

}
