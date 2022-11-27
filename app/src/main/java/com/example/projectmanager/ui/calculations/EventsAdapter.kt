package com.example.projectmanager.ui.calculations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.ui.data.DataViewModel
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.data.TableAdapter
import com.example.projectmanager.ui.renameme.Work

class EventsAdapter(private val calculationsViewModel: DataViewModel): RecyclerView.Adapter<EventsAdapter.ItemViewHolder>() {
    //тупо, но для начала пусть так


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_basic_item, parent, false)//добавить выбор мода
        return EventsAdapter.ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: EventsAdapter.ItemViewHolder, position: Int) {
        val item = calculationsViewModel.calculator.nodeData[position]
        holder.column1.text=item.number.toString()
        holder.column2.text=item.earlyTime.toString()
        holder.column3.text=item.lateTime.toString()
        holder.column4.text=item.reservedTime.toString()
    }

    override fun getItemCount(): Int {
        return calculationsViewModel.calculator.nodeData.size
    }

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        // val materialCardView: MaterialCardView = itemView.findViewById(R.id.data_card_view)
        val row: TableRow =itemView.findViewById(R.id.events_row)
        val column1: TextView =itemView.findViewById(R.id.events_column_1_value)
        val column2: TextView =itemView.findViewById(R.id.events_column_2_value)
        val column3: TextView =itemView.findViewById(R.id.events_column_3_value)
        val column4: TextView =itemView.findViewById(R.id.events_column_4_value)
    }
}