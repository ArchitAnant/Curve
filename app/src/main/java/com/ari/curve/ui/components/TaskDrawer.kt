package com.ari.curve.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ari.curve.data.dao.Task
import com.ari.curve.regular_font
import com.ari.curve.semibold_font

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDrawer(task: Task, onDismissRequest: ()-> Unit,markDone:(String)->Unit,modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()
    val uriHandler = LocalUriHandler.current

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
        containerColor = Color(0xFF121212)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)) {
            Text(
                text = "${task.taskTitle}",
                fontFamily = semibold_font,
                fontSize = 23.sp,
                color = Color.White
            )
            if (task.taskDesc.isNotEmpty()) {
                Text(
                    text = task.taskDesc,
                    fontFamily = regular_font,
                    fontSize = 15.sp,
                    color = Color.White.copy(0.5f),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            if (task.taskList.isNotEmpty()){
                Text(
                    text = "Itinerary",
                    fontFamily = semibold_font,
                    fontSize = 20.sp,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier.padding(top = 20.dp)
                )
                LazyColumn {
                    items(task.taskList.size){
                        Text(
                            text = task.taskList[it],
                            fontFamily = regular_font,
                            fontSize = 15.sp,
                            color = Color.White.copy(0.5f),
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            ) {
                if (task.attachments.isNotEmpty()){
                    LazyRow(
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).weight(0.7f)
                            .padding(end = 10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {


                        items(task.attachments.size) {
                            Text(
                                text = task.attachments[it].link_name,
                                fontFamily = regular_font,
                                fontSize = 15.sp,
                                color = Color.White,
                                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(0.1f)).padding(10.dp)
                                    .clickable{
                                            uriHandler.openUri(task.attachments[it].url)

                                    }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
                else{
                    Spacer(modifier = Modifier.weight(0.7f))
                }

                Button(
                    onClick = {
                        markDone(task.taskId)
                    },
                    modifier = Modifier.height(55.dp),   // SAME HEIGHT AS TEXT FIELD
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Mask as done",
                        fontFamily = semibold_font,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun TaskPrev() {
//    TaskDrawer(temp_task,{})
}