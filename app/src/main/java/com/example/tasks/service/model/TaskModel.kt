package com.example.tasks.service.model

import com.google.gson.annotations.SerializedName

class TaskModel(
    @SerializedName("Id")
    val id: Int = 0,
    @SerializedName("PriorityId")
    val priorityId: Int,
    @SerializedName("Description")
    val description: String,
    @SerializedName("DueDate")
    val data: String,
    @SerializedName("Complete")
    val complete: Boolean = false
) {
}