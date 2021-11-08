package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tasks.Validation
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application)
    private val taskRepository = TaskRepository(application)

    private val _validation = MutableLiveData<Validation>()
    val validation: LiveData<Validation> get() = _validation

    private val _listPriority = MutableLiveData<List<PriorityModel>>()
    val listPriority: LiveData<List<PriorityModel>> get() = _listPriority

    private val _taskModel = MutableLiveData<TaskModel>()
    val taskModel: LiveData<TaskModel> get() = _taskModel

    fun listPrioritys() {
        _listPriority.value = priorityRepository.listPriority()
    }

    fun save(task: TaskModel) {
        viewModelScope.launch {
            if(task.id == 0) {
                taskRepository.create(task) { sucess, error ->
                    when {
                        onSucess(sucess) -> {
                            _validation.value = Validation(true)
                        }

                        onFailure(error) -> {
                            _validation.value = Validation(false, error!!)
                        }
                    }
                }
            } else {
                taskRepository.update(task) { sucess, error ->
                    when {
                        onSucess(sucess) -> {
                            _validation.value = Validation(true)
                        }

                        onFailure(error) -> {
                            _validation.value = Validation(false, error!!)
                        }
                    }
                }
            }
        }



    }

    fun load(taskId: Int) {
        viewModelScope.launch {
            taskRepository.load(taskId) { task, error ->
                when {
                    task != null -> {
                        _taskModel.value = task

                    }
                    error != null -> {
                        _validation.value = Validation(false, error)
                    }
                }
            }
        }
    }


    private fun onFailure(error: String?) = !error.isNullOrEmpty()

    private fun onSucess(success: Boolean) = success



}