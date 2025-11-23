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

    @SerialName("tasklist/itinerary")
    val taskList: List<String> = emptyList(),

    @SerialName("date_and_time")
    val dateAndTime: String="",

    @SerialName("attachments")
    val attachments: Map<String, String> = mapOf()
)

@Serializable
data class Attachments(
    val link: String? = null
)

//"""
//{
//  "HACKATHON_REG_001": {
//    "task_title": "Inquire about and Register for Agentathon 2025",
//    "task_desc": "Ask people about the hackathon and complete registration for Agentathon 2025 - Biggest Agentic AI Hackathon of Hyderabad at Google Developer Groups GDG Hyderabad.",
//    "tasklist/itinerary": [
//      "Ask people about the hackathon",
//      "Register for Agentathon 2025"
//    ],
//    "date and time": "2025-11-23T20:00:00",
//    "attachments": {
//      "link": "https://share.google/0VhVDBmznrQgndsU1"
//    }
//  }
//}
//"""

//val temp_task = Task(
//    taskId = "20251123T190000_RegisterAndInquireAgentathon",
//    taskTitle = "Inquire about and Register for Agentathon 2",
//    taskDesc = "Ask people about the hackathon and complete registration for Agentathon ",
//    taskList = listOf("Ask people about the hackathon", "Register for Agentathon"),
//    dateAndTime = "2025-11-23T20:00:00",
//    attachments = mapOf("yep":Attachments("https://share.google/0VhVDBmznrQgndsU1"))
//)