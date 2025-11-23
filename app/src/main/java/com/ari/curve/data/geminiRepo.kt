package com.ari.curve.data

import com.ari.curve.data.dao.Task
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.time.LocalDateTime



// Initialize the Gemini Developer API backend service
// Create a `GenerativeModel` instance with a model that supports your use case


class GeminiRepo(){
    val systemInstructionContent = content {
        text(
            """
        Time now : ${LocalDateTime.now()}
        You are a smart task extraction model. You task is to follow the input text and extract Task, time and any attached items it(images, links..etc).
You are supposed to return a strict json like this
{
'taskid':{
'taskid',
task_title,
task_desc(not nessesary),
tasklist/itinerary(empty if there is only one task),
date_and_time,
attachments:{
'image/link/...':
}
}
'taskid':{
....}
....
}

you are supposed to generate a context aware taskid and make sure it is reproducible and unique.
your json would be used in the android app to schedule an alarm. club tasks at same time together. Use ISO format for the data and time. for the tasklist/itinerary make it discrete or only phrases. if the tasks can be fully described by the title leave the desc empty. generate only one task
        """.trimIndent()
        )
    }



    // Initialize your GenerativeModel with both system instructions and generation config for JSON output
    val model: GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-2.5-flash",
            systemInstruction = systemInstructionContent, // Pass the Content object here
            generationConfig = generationConfig {
                responseMimeType = "application/json" // Force JSON output
            }
        )

    suspend fun generateTasks(userInput: String): List<Task> {

        // complete this
        val response = model.generateContent(userInput)

        if (response.text.isNullOrEmpty()){
            return listOf()
        }
        val rawJson = response.text!!.trim()
//        println("Raw JSON Received: $rawJson")

        // 4. Deserialize the response text into the expected Map structure
        return try {
            // Deserialize the expected structure: Map<String, Task>
            val taskMap = Json.decodeFromString(
                deserializer = MapSerializer(
                    String.serializer(),
                    Task.serializer()
                ),
                string = rawJson
            )

            // Return the values (the list of tasks) from the map
            taskMap.values.toList()

        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse Gemini JSON response. Check if the model output strictly follows the required JSON map structure.\nError: ${e.localizedMessage}\nRaw Text: $rawJson", e)
        }
    }
}
