package com.example.projectmanager.ui.calculations

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.ui.data.DataViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.NonDisposableHandle.parent
import java.nio.file.Files.find

//class ChildRVTouch(val child:View?,val parent: RecyclerView):View.OnTouchListener{
//    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//        if(p0?.id==child?.id){
//            if(MotionEventCompat.getActionMasked(p1)==MotionEvent.ACTION_UP){
//                parent.requestDisallowInterceptTouchEvent(false)
//            }
//            else{
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//        }
//        return View.OnTouchListener() .onTouch(p0,p1)
//    }
//
//}

class SuperAdapter(private val calculationsViewModel: DataViewModel, val activity: FragmentActivity?, val recyclerParent: RecyclerView):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolderWorksRecycler(itemView: View): RecyclerView.ViewHolder(itemView){
        val recycler:RecyclerView = itemView.findViewById(R.id.works_recycler_view)
    }
    class ItemViewHolderEventRecycler(itemView: View): RecyclerView.ViewHolder(itemView){
        val recycler:RecyclerView = itemView.findViewById(R.id.events_recycler_view)
    }
    class ItemViewHolderPath(itemView: View): RecyclerView.ViewHolder(itemView){
        val input:TextInputEditText = itemView.findViewById(R.id.path_input_edit_text)
        val tensionValue:TextView = itemView.findViewById(R.id.path_tension_value)
        val reservedTimeValue:TextView = itemView.findViewById(R.id.path_reserved_time_value)
        val button:Button = itemView.findViewById(R.id.calculate_button)
    }
    class ItemViewHolderCriticalPath(itemView: View): RecyclerView.ViewHolder(itemView){
        val pathValue:TextView = itemView.findViewById(R.id.path_path_value)
        val lengthValue:TextView = itemView.findViewById(R.id.path_length_value)
        val dispersionValue:TextView = itemView.findViewById(R.id.path_dispersion_value)
    }
    class ItemViewHolderTimeFromProb(itemView: View): RecyclerView.ViewHolder(itemView){
        val input:TextInputEditText = itemView.findViewById(R.id.path_input_edit_text)
        val timeValue:TextView = itemView.findViewById(R.id.time_from_prob_value)
        val button:Button = itemView.findViewById(R.id.calculate_button)
    }
    class ItemViewHolderProbFromTime(itemView: View): RecyclerView.ViewHolder(itemView){
        val input:TextInputEditText = itemView.findViewById(R.id.path_input_edit_text)
        val probValue:TextView = itemView.findViewById(R.id.prob_from_time_value)
        val button:Button = itemView.findViewById(R.id.calculate_button)
    }
    class ItemViewHolderInterval(itemView: View): RecyclerView.ViewHolder(itemView){
        val input:TextInputEditText = itemView.findViewById(R.id.path_input_edit_text)
        val intevalValue:TextView = itemView.findViewById(R.id.interval_value)
        val button:Button = itemView.findViewById(R.id.calculate_button)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
        return when(viewType){
            0->ItemViewHolderWorksRecycler(adapterLayout.inflate(R.layout.works_table_basic, parent, false))
            1->ItemViewHolderEventRecycler( adapterLayout.inflate(R.layout.event_table_basic,parent,false))
            2->ItemViewHolderPath(adapterLayout.inflate(R.layout.super_item_input_and_textview,parent,false))
            3->ItemViewHolderCriticalPath(adapterLayout.inflate(R.layout.super_item_critical_path, parent, false))
            4->ItemViewHolderTimeFromProb( adapterLayout.inflate(R.layout.super_item_time_from_prob,parent,false))
            5->ItemViewHolderProbFromTime(adapterLayout.inflate(R.layout.super_item_prob_from_time,parent,false))
            6->ItemViewHolderInterval(adapterLayout.inflate(R.layout.super_item_trust_interval,parent,false))
            else->throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolderWorksRecycler) {
            holder.recycler.adapter = WorksAdapter(calculationsViewModel)
            holder.recycler.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            holder.recycler.setOnTouchListener(object: View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    if (p0?.id == holder.recycler?.id) {
                        if (MotionEventCompat.getActionMasked(p1) == MotionEvent.ACTION_UP) {
                            recyclerParent.requestDisallowInterceptTouchEvent(false)
                        } else {
                            recyclerParent.requestDisallowInterceptTouchEvent(true)
                        }
                    }
                    return p0?.onTouchEvent(p1) ?: true
                }
            })
        }
        if(holder is ItemViewHolderEventRecycler){
            holder.recycler.adapter=EventsAdapter(calculationsViewModel)
            holder.recycler.layoutManager= LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
            holder.recycler.setOnTouchListener(object: View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    if (p0?.id == holder.recycler?.id) {
                        if (MotionEventCompat.getActionMasked(p1) == MotionEvent.ACTION_UP) {
                            recyclerParent.requestDisallowInterceptTouchEvent(false)
                        } else {
                            recyclerParent.requestDisallowInterceptTouchEvent(true)
                        }
                    }
                    return p0?.onTouchEvent(p1) ?: true
                }
            })
        }
        if(holder is ItemViewHolderPath){
            holder.button.setOnClickListener {
                try {
                    val path:List<Int> = holder.input.text?.split(",")!!.map { it.toInt() }
                    val arcInfo=calculationsViewModel.calculator.getArcInfo(path)
                    holder.tensionValue.text=arcInfo.tension.toString()
                    holder.reservedTimeValue.text=arcInfo.reservedTime.toString()
                }
                catch (e:Exception){
                    Toast.makeText(activity,"неверный ввод", Toast.LENGTH_SHORT)
                }
            }
        }
        if(holder is ItemViewHolderCriticalPath){
            holder.dispersionValue.text=calculationsViewModel.calculator.getCriticalPathDispersion().toString()
            holder.lengthValue.text=calculationsViewModel.calculator.nodeData.last().earlyTime.toString()//сделать норм метод
            holder.pathValue.text=calculationsViewModel.calculator.getCriticalPaths().toString()
        }
        if(holder is ItemViewHolderInterval){
            holder.button.setOnClickListener {
                try {
                    val prob:Double = holder.input.text?.toString()!!.toDouble()
                    val interval=calculationsViewModel.calculator.getInterval(prob)
                    holder.intevalValue.text="[${interval.first};${interval.second}]"
                }
                catch (e:Exception){
                    Toast.makeText(activity,"неверный ввод", Toast.LENGTH_SHORT)
                }
            }
        }
        if(holder is ItemViewHolderTimeFromProb){
            holder.button.setOnClickListener {
                try {
                    val prob:Double = holder.input.text?.toString()!!.toDouble()
                    try {
                        val time=calculationsViewModel.calculator.timeFromProbability(prob)
                        holder.timeValue.text=time.toString()
                    }catch (e:ArithmeticException){
                        holder.timeValue.text="Infinity"
                    }

                }
                catch (e:Exception){
                    Toast.makeText(activity,"неверный ввод", Toast.LENGTH_SHORT)
                }
            }
        }
        if(holder is ItemViewHolderProbFromTime){
            holder.button.setOnClickListener {
                try {
                    val time:Int = holder.input.text?.toString()!!.toInt()
                    val prob=calculationsViewModel.calculator.probabilityUnderTimeOf(time)
                    holder.probValue.text=prob.toString()
                }
                catch (e:Exception){
                    Toast.makeText(activity,"неверный ввод", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0->0
            1->1
            2->2
            3->3
            4->4
            5->5
            6->6
            else->6//потом будем считать нормально, когда переделывать ресайлеры вложенные, а то они не работают, грустно
        }
    }
}