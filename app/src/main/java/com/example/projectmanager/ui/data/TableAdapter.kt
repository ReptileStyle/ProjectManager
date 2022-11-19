package com.example.projectmanager.ui.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.ui.renameme.*
import com.google.android.material.card.MaterialCardView
import java.util.LinkedList


class TableAdapter(
    private val workList: List<Work>
):RecyclerView.Adapter<TableAdapter.ItemViewHolder>() {
   // private val workList:MutableList<Work> = LinkedList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item=workList[position]
//        holder.materialCardView.apply {
//            radius=8f
//            strokeWidth=2
//        }
        holder.row.apply {
            if (position%2==1) setBackgroundColor(context.getColor(R.color.green))
            else setBackgroundColor(context.getColor(R.color.green_light))
        }
        holder.columnName.text=item.name
        holder.columnDuration.text=item.duration.toString()
        holder.columnRequiredWorks.text=item.requiredWorks.toStr()
    }


    override fun getItemCount(): Int {
        return workList.count()
    }

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
       // val materialCardView: MaterialCardView = itemView.findViewById(R.id.data_card_view)
        val row: TableRow=itemView.findViewById(R.id.data_row)
        val columnName: TextView =itemView.findViewById(R.id.data_column_1_value)
        val columnDuration: TextView =itemView.findViewById(R.id.data_column_2_value)
        val columnRequiredWorks: TextView =itemView.findViewById(R.id.data_column_3_value)
    }
}