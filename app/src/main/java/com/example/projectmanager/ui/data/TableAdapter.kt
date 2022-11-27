package com.example.projectmanager.ui.data

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.ui.renameme.*
import com.google.android.material.card.MaterialCardView
import java.util.LinkedList


class TableAdapter(
    private val viewModel: DataViewModel,
    val mode:Int,
    val activity: Context
):RecyclerView.Adapter<TableAdapter.ItemViewHolder>() {
   // private val workList:MutableList<Work> = LinkedList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item=viewModel.getDataset()[position]
//        holder.materialCardView.apply {
//            radius=8f
//            strokeWidth=2
//        }
        holder.row.apply {
            if (position%2==1) setBackgroundColor(context.getColor(R.color.green))
            else setBackgroundColor(context.getColor(R.color.green_light))
        }
        when(mode){
            1->{
                holder.columnDurationOptimistic.visibility=View.GONE
                holder.columnDurationPessimistic.visibility=View.GONE
                holder.columnDuration.visibility=View.VISIBLE
            }
            2->{
                holder.columnDurationOptimistic.visibility=View.VISIBLE
                holder.columnDurationPessimistic.visibility=View.VISIBLE
                holder.columnDuration.visibility=View.GONE
            }
            3->{
                holder.columnDurationOptimistic.visibility=View.VISIBLE
                holder.columnDurationPessimistic.visibility=View.VISIBLE
                holder.columnDuration.visibility=View.VISIBLE
            }
        }
        holder.columnName.text=item.name
        holder.columnDuration.text=item.duration.toString()
        holder.columnDurationOptimistic.text=item._durationOptimistic.toString()
        holder.columnDurationPessimistic.text=item._durationPessimistic.toString()
        holder.columnRequiredWorks.text=item.requiredWorks.toStr()
        holder.row.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setMessage(R.string.delete_work)
                .setPositiveButton("удалить") { dialog, i ->
                    viewModel.removeWork(item.name)
                    holder.row.visibility=View.GONE
                }
                .setNegativeButton("отмена") { dialog, i ->

                }
                .create().show()
        }
    }


    override fun getItemCount(): Int {
        return viewModel.getDataset().count()
    }

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
       // val materialCardView: MaterialCardView = itemView.findViewById(R.id.data_card_view)
        val row: TableRow=itemView.findViewById(R.id.data_row)
        val columnName: TextView =itemView.findViewById(R.id.data_column_1_value)
        val columnDurationPessimistic: TextView =itemView.findViewById(R.id.data_column_2_value)
        val columnDuration: TextView =itemView.findViewById(R.id.data_column_3_value)
        val columnDurationOptimistic: TextView =itemView.findViewById(R.id.data_column_4_value)
        val columnRequiredWorks: TextView =itemView.findViewById(R.id.data_column_5_value)
    }
}