package com.ari.curve.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ari.curve.data.dao.Task
import com.ari.curve.regular_font
import com.ari.curve.semibold_font
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatDateTime(raw: String): String {
    // Parse local datetime (no timezone)
    val localDT = LocalDateTime.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    // Convert to system zone (assumes the input is already local time)
    val zoned = localDT.atZone(ZoneId.systemDefault())

    val output = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
    return zoned.format(output)
}


@Composable
fun TaskItem(task: Task, onExpandClick:(Task)->Unit, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxWidth().background(Color.Gray.copy(0.2f)).clickable{
        onExpandClick(task)
    }.padding(20.dp)){

        Box(
            modifier = Modifier
                .size(15.dp)
                .background(Color.White, shape = CircleShape)
        )
        Spacer(modifier= Modifier.height(8.dp))
        Text(
            text = task.taskTitle,
            color = Color.White,
            modifier = Modifier.padding(bottom = 10.dp, top = 5.dp),
            fontFamily = semibold_font,
            fontSize = 20.sp
        )
        if (task.taskDesc.isNotEmpty()) {
            Text(
                text = task.taskDesc,
                color = Color.White.copy(0.7f),
                modifier = Modifier.padding(bottom = 10.dp),
                fontFamily = regular_font,
                fontSize = 14.sp
            )
        }
        Row (modifier= Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = formatDateTime(task.dateAndTime),
                color = Color.White.copy(0.5f),
                modifier = Modifier.padding(bottom = 10.dp),
                fontFamily = regular_font,
                fontSize = 12.sp
            )
            Spacer(modifier= Modifier.weight(1f))
            if (task.attachments.isNotEmpty()){
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.White.copy(0.7f), shape = CircleShape)
                )
                Spacer(modifier= Modifier.width(5.dp))
                Text(
                    text = "Attached",
                    color = Color.White.copy(0.7f),
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = regular_font,
                    fontSize = 9.sp
                )
            }

        }
    }
}

@Preview
@Composable
private fun TaskPrev() {
//    TaskItem(temp_task,{})
}