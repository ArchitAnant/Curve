package com.ari.curve.data.dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("taskid")
    val taskId: String = "",

    @SerialName("task_title")
    val taskTitle: String="",

    @SerialName("task_desc")
    val taskDesc: String="",

    @SerialName("tasklist")
    val taskList: List<String> = emptyList(),

    @SerialName("date_and_time")
    val dateAndTime: String="",

    @SerialName("attachments")
    val attachments: List<Attachments> = listOf()
)

@Serializable
data class Attachments(
    val link_name: String = "",
    val url: String = ""
)