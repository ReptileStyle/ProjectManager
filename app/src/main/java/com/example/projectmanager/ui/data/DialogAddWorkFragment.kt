package com.example.projectmanager.ui.data

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.projectmanager.R
import com.example.projectmanager.ui.renameme.Work

class DialogAddWorkFragment(val viewModel: DataViewModel) : DialogFragment() {

    lateinit var myDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val inflater: LayoutInflater = requireActivity().layoutInflater
            val view: View = inflater.inflate(R.layout.data_dialog_add_item_basic, null, false)
            val column1: EditText = view.findViewById(R.id.data_dialog_value_1)
            val column2: EditText = view.findViewById(R.id.data_dialog_value_2)
            val column3: EditText = view.findViewById(R.id.data_dialog_value_3)
            val column4: EditText = view.findViewById(R.id.data_dialog_value_4)
            val column5: EditText = view.findViewById(R.id.data_dialog_value_5)
            when (viewModel.currentMode) {
                1 -> {
                    column2.visibility = View.GONE
                    column3.visibility = View.VISIBLE
                    column4.visibility = View.GONE
                }
                2 -> {
                    column2.visibility = View.VISIBLE
                    column3.visibility = View.GONE
                    column4.visibility = View.VISIBLE
                }
                3 -> {
                    column2.visibility = View.VISIBLE
                    column3.visibility = View.VISIBLE
                    column4.visibility = View.GONE
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder
                .setView(view)
                .setMessage(R.string.dialog_add_work_header)
                .setPositiveButton(R.string.button_add_work,
                    DialogInterface.OnClickListener { dialog, id ->
                        when (viewModel.currentMode) {
                            1 -> viewModel.addWork(
                                Work(
                                    name = column1.text.toString(),
                                    duration = column3.text.toString().toInt(),
                                    requiredWorks = getWorkListFromString(column5.text.toString()).toMutableList()
                                )
                            )
                            2 -> viewModel.addWork(
                                Work(
                                    column1.text.toString(),
                                    durationOptimistic = column2.text.toString().toInt(),
                                    durationPessimistic = column4.text.toString().toInt(),
                                    requiredWorks = getWorkListFromString(column5.text.toString()).toMutableList()
                                )
                            )
                            3 -> viewModel.addWork(
                                Work(
                                    name = column1.text.toString(),
                                    duration = column2.text.toString().toInt(),
                                    durationOptimistic = column3.text.toString().toInt(),
                                    durationPessimistic = column4.text.toString().toInt(),
                                    requiredWorks = getWorkListFromString(column5.text.toString()).toMutableList()
                                )
                            )
                        }
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun getWorkListFromString(list: String): List<Work> {
        val workList: MutableList<Work> = mutableListOf()
        list.split(",").forEach { name ->
            workList.add(viewModel.getDataset().find { it.name == name }!!)
        }
        return workList
    }

}

