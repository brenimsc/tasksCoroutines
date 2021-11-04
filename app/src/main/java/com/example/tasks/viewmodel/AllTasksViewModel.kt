package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.Validation
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)
    private var taskFilter = 0

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> get() = _tasks

    private val _validation = MutableLiveData<Validation>()
    val validation: LiveData<Validation> get() = _validation

    fun list(taskFilter: Int) {
        this.taskFilter = taskFilter

        when (this.taskFilter) {

            TaskConstants.FILTER.ALL -> {
                taskRepository.all { list, error ->
                    when {
                        list.isNullOrEmpty()-> {
                            _tasks.value = arrayListOf()
                            error?.let {
                                _validation.value = Validation(false, it)
                            }
                        }
                        else -> {
                            _tasks.value = list
                        }
                    }
                }
            }

            TaskConstants.FILTER.NEXT -> {
                taskRepository.nextWeek { list, error ->
                    when {
                        list.isNullOrEmpty()-> {
                            _tasks.value = arrayListOf()
                            error?.let {
                                _validation.value = Validation(false, it)
                                return@nextWeek
                            }
                            _validation.value = Validation(false, "Não foi possível realizar buscas")
                        }
                        else -> {
                            _tasks.value = list
                        }
                    }
                }
            }

            else -> {
                taskRepository.overdue { list, error ->
                    when {
                        list.isNullOrEmpty()-> {
                            _tasks.value = arrayListOf()
                            error?.let {
                                _validation.value = Validation(false, it)
                            }
                        }
                        else -> {
                            _tasks.value = list
                        }
                    }
                }
            }

        }

    }

    fun onComplete(id: Int) {
        taskRepository.updateStatus(id, true) { sucess, error ->
            when {
                sucess != null -> {
                    list(taskFilter)
                }
            }
        }

    }

    fun onUndo(id: Int) {
        taskRepository.updateStatus(id, false) { sucess, error ->
            when {
                sucess != null -> {
                    list(taskFilter)
                }
            }
        }
    }

    fun delete(id: Int) {
        taskRepository.delete( id) { sucess, error ->
            if (sucess) {
                list(taskFilter)
                _validation.value = Validation(true)
            } else {
                _validation.value = Validation(false, error ?: "Houve um erro")
            }

        }
    }


}