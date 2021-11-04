package com.example.tasks.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.databinding.FragmentAllTasksBinding
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.ui.adapter.TaskAdapter
import com.example.tasks.viewmodel.AllTasksViewModel

class AllTasksFragment : Fragment() {

    private lateinit var binding: FragmentAllTasksBinding
    private lateinit var mViewModel: AllTasksViewModel
    private val mAdapter = TaskAdapter()
    private var taskFilter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this).get(AllTasksViewModel::class.java)

        setupArguments()

        setupRecycler()

        listeners()

        observe()

        return binding.root
    }

    private fun setupArguments() {
        arguments?.let {
            taskFilter = it.getInt(TaskConstants.BUNDLE.TASKFILTER, 0)
        }
    }

    private fun setupRecycler() {
        val recycler = binding.recyclerAllTasks
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter
    }

    private fun listeners() {
        mAdapter.setOnClickListener { task, onLongClick ->
            if (onLongClick) {
                mViewModel.delete(task.id)
            } else {
                val intent = Intent(context, TaskFormActivity::class.java)
                intent.putExtra(TaskConstants.BUNDLE.TASKID, task.id)
                startActivity(intent)
            }

        }
        mAdapter.setOnClickListenerTaskComplete { id, complete ->
            if (complete) {
                mViewModel.onUndo(id)
            } else {
                mViewModel.onComplete(id)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.list(taskFilter)
    }

    private fun observe() {
        mViewModel.tasks.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mAdapter.updateList(it)
            }
        }
        mViewModel.validation.observe(viewLifecycleOwner) {
            if (it.status) {
                Toast.makeText(requireContext(), getString(R.string.task_removed), Toast.LENGTH_SHORT).show()
            } else {
                Log.e("BRENOL", it.message)
                val toast = Toast.makeText(requireContext(), it.message,  Toast.LENGTH_SHORT)
//                val textMessage = toast.view?.findViewById<TextView>(android.R.id.message)
//                textMessage?.setTextColor(Color.RED)
//                textMessage?.gravity = Gravity.TOP
                toast.view = layoutInflater.inflate(R.layout.layout_toast, null)
                toast.show()
            }
        }
    }

}
