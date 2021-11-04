package com.example.tasks.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.tasks.R
import com.example.tasks.databinding.ActivityTaskFormBinding
import com.example.tasks.formatDateToDate
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding
    private lateinit var mViewModel: TaskFormViewModel
    private val dateformat = SimpleDateFormat("dd/MM/yyyy")
    private val listPriorityId = mutableListOf<Int>()
    private var mTaskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)


        setupButtons()
        observe()

        mViewModel.listPrioritys()

        loadDataFromActivity()

    }

    private fun loadDataFromActivity() {
        Log.e("BRENOL", "Chegou aqui ")
        val bundle = intent.extras
        bundle?.let {
            Log.e("BRENOL", "Chegou aqui ${bundle.getInt(TaskConstants.BUNDLE.TASKID)}")
            mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            mViewModel.load(mTaskId)
            binding.buttonSave.text = getString(R.string.update_task)
        }
    }


    private fun observe() {
        mViewModel.listPriority.observe(this) {
            val list: MutableList<String> = mutableListOf()
            for (item in it) {
                list.add(item.description)
                listPriorityId.add(item.id)
            }
            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }
        mViewModel.validation.observe(this) {
            if (it.status) {
                if (mTaskId == 0) {
                    toast(getString(R.string.task_created))
                } else {
                    toast(getString(R.string.task_updated))
                }
                finish()
            } else {
                toast(it.message)
            }
        }
        mViewModel.taskModel.observe(this) {
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete
            binding.buttonDate.text = it.data.formatDateToDate()
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))
        }
    }

    private fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (i in 0 until listPriorityId.count()) {
            if (listPriorityId[i] == priorityId) {
                index = i
                break
            }
        }
        return index
    }

    private fun setupButtons() {
        binding.buttonSave.setOnClickListener {
            handleSave()
        }

        binding.buttonDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun handleSave() {
        val description = binding.editDescription.text.toString()
        val complete = binding.checkComplete.isChecked
        val data = binding.buttonDate.text.toString()
        val priorityId = listPriorityId[binding.spinnerPriority.selectedItemPosition] // selecionando de acordo com a sequencia que colocamos na listPriority
        val task = TaskModel(id = mTaskId, description = description, complete = complete, data = data, priorityId = priorityId)
        mViewModel.save(task)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            val dateString = dateformat.format(date.time)
            binding.buttonDate.text = dateString
        }, year, month, day).show()
    }

}
