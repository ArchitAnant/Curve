package com.ari.curve.data

import com.ari.curve.data.dao.Task
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.StringFormat
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.serialization.json.Json
import java.time.LocalDateTime


class GeminiRepo(){
    val taskSchema = Schema.obj(
        mapOf(
            "taskid" to Schema.string(),
            "task_title" to Schema.string(),
            "task_desc" to Schema.string(),
            "tasklist" to Schema.array(Schema.string()),
            "date_and_time" to Schema.string(format = StringFormat.Custom("yyyy-MM-dd'T'HH:mm:ss")),

            "attachments" to Schema.array(
                Schema.obj(
                    mapOf(
                        "link_name" to Schema.string(description = "The generative name/label for the link"),
                        "url" to Schema.string(description = "The actual URL string")
                    )
                )
            )
        )
    )



    val systemInstructionContent = content {
        text(
            """
        Time now : ${LocalDateTime.now()}
        role: You are a smart task extraction model. You task is to follow the input text and extract Task, time and any attached links.You are supposed to return a strict json
        
        Job:
you are supposed to generate a context aware taskid and make sure it is reproducible and unique. club tasks at same time together. 
for the tasklist make it discrete or only phrases but each task should be there, if the tasklist is not required keep it empty. if the tasks can be fully described by the title leave the desc empty
if the user mentions dates,days,time,names or any other distinct features mention them.
don't be repetitive in the fields. you are allowed to create more than 1 task but don't create useless ones.
        """.trimIndent()
        )
    }



    // Initialize your GenerativeModel with both system instructions and generation config for JSON output
    val model: GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-2.5-flash",
            systemInstruction = systemInstructionContent,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema =
                    Schema.array(
                        taskSchema
                    )
            }
        )


    suspend fun generateTasks(userInput: String): List<Task> {
        val response = model.generateContent(userInput)

        if (response.text.isNullOrEmpty()){
            return listOf()
        }
        val rawJson = response.text!!.trim()
        return try {
            Json.decodeFromString<List<Task>>(rawJson)


        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse Gemini JSON response. Check if the model output strictly follows the required JSON map structure.\nError: ${e.localizedMessage}\nRaw Text: $rawJson", e)
        }
    }
}
