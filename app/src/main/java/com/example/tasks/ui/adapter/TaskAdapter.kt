package com.example.tasks.ui.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import java.text.SimpleDateFormat

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {



    private var mList: List<TaskModel> = arrayListOf()
    private lateinit var callback : (task: TaskModel, onLongClick: Boolean) -> Unit
    private lateinit var callbackTaskComplete : (id: Int, complete: Boolean) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.row_task_list, parent, false)
        return TaskViewHolder(item, callback, callbackTaskComplete)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun setOnClickListener(callback : (task: TaskModel, onLongClick: Boolean) -> Unit) {
        this.callback = callback
    }



    fun setOnClickListenerTaskComplete(callbackTaskComplete : (id: Int, complete: Boolean) -> Unit) {
        this.callbackTaskComplete = callbackTaskComplete
    }


    fun updateList(list: List<TaskModel>) {
        mList = list
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View, var callback: (task: TaskModel, onLongClick: Boolean) -> Unit, var callbackTaskComplete : (id: Int, complete: Boolean) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        private val priorityRepository = PriorityRepository(itemView.context)

        private var mTextDescription: TextView = itemView.findViewById(R.id.text_description)
        private var mTextPriority: TextView = itemView.findViewById(R.id.text_priority)
        private var mTextDueDate: TextView = itemView.findViewById(R.id.text_due_date)
        private var mImageTask: ImageView = itemView.findViewById(R.id.image_task)


        fun bindData(taskModel: TaskModel) {

            this.mTextDescription.text = taskModel.description
            this.mTextPriority.text = priorityRepository.getDescription(taskModel.priorityId)

            val date = SimpleDateFormat("yyyy-MM-dd").parse(taskModel.data)
            this.mTextDueDate.text = dateFormat.format(date)

            if (taskModel.complete) {
                mTextDescription.setTextColor(Color.GRAY)
                mImageTask.setImageResource(R.drawable.ic_done)
            } else {
                mTextDescription.setTextColor(Color.BLACK)
                mImageTask.setImageResource(R.drawable.ic_todo)
            }


            itemView.setOnClickListener {
                callback(taskModel, false)
            }

            mImageTask.setOnClickListener {
                if (taskModel.complete) {
                    callbackTaskComplete(taskModel.id, true)
                } else {
                    callbackTaskComplete(taskModel.id, false)
                }
            }

            mTextDescription.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle(R.string.remocao_de_tarefa)
                    .setMessage(R.string.remover_tarefa)
                    .setPositiveButton(R.string.sim) { dialog, which ->
                        callback(taskModel, true)
                    }
                    .setNeutralButton(R.string.cancelar, null)
                    .show()
                true
            }

        }

    }

}