package com.example.projectmanager.ui.calculations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.data.TableAdapter
import com.example.projectmanager.ui.renameme.Work

class WorksAdapter(private val calculationsViewModel: CalculationsViewModel): RecyclerView.Adapter<WorksAdapter.ItemViewHolder>() {
    //тупо, но для начала пусть так


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorksAdapter.ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.works_basic_item, parent, false)//добавить выбор мода
        return WorksAdapter.ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: WorksAdapter.ItemViewHolder, position: Int) {
        val item = calculationsViewModel.calculator.edgeData[position]
        holder.column1.text=item.toString()
        holder.column2.text=item.value.toString()
        holder.column3.text=item.dispersion.toString()
        holder.column4.text=item.earlyTime.toString()
        holder.column5.text=(item.lateTime-item.value).toString()
        holder.column6.text=(item.earlyTime+item.value).toString()
        holder.column7.text=item.lateTime.toString()
        holder.column8.text=item.reservedTime.toString()
        holder.column9.text=item.reservedTimeIndependent.toString()

    }

    override fun getItemCount(): Int {
        return calculationsViewModel.calculator.edgeData.size
    }

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        // val materialCardView: MaterialCardView = itemView.findViewById(R.id.data_card_view)
        val row: TableRow =itemView.findViewById(R.id.works_row)
        val column1: TextView =itemView.findViewById(R.id.works_column_1_value)
        val column2: TextView =itemView.findViewById(R.id.works_column_2_value)
        val column3: TextView =itemView.findViewById(R.id.works_column_3_value)
        val column4: TextView =itemView.findViewById(R.id.works_column_4_value)
        val column5: TextView =itemView.findViewById(R.id.works_column_5_value)
        val column6: TextView =itemView.findViewById(R.id.works_column_6_value)
        val column7: TextView =itemView.findViewById(R.id.works_column_7_value)
        val column8: TextView =itemView.findViewById(R.id.works_column_8_value)
        val column9: TextView =itemView.findViewById(R.id.works_column_9_value)
    }
}