package com.example.tasks.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "priority")
class PriorityModel(
    @SerializedName("Id")
    @PrimaryKey
    val id: Int,
    @SerializedName("Description")
    val description: String) {


}