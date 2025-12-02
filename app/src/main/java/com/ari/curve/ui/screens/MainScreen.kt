package com.ari.curve.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.ari.curve.data.FirebaseManager
import com.ari.curve.data.dao.Task
import com.ari.curve.data.dao.User
import com.ari.curve.regular_font
import com.ari.curve.semibold_font
import com.ari.curve.ui.components.TaskDrawer
import com.ari.curve.ui.components.TaskItem
import com.ari.curve.ui.viewmodels.MainViewModel

@Composable
fun MainScreen(vm : MainViewModel,modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        vm.observeChat()
    }
    var message by remember { mutableStateOf("") }
    val tasks by vm.tasks.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            Text(
                text = "Tasks",
                color = Color.White,
                fontFamily = semibold_font,
                fontSize = 35.sp,
                modifier=modifier.padding(start = 20.dp, top = 20.dp)
            )
        },
        bottomBar = {
            Row {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = {
                        Text(
                            text = "Type your message here",
                            color = Color.White.copy(0.3f),
                            fontFamily = regular_font,
                            fontSize = 18.sp,
                        )
                    },
                    modifier = modifier.fillMaxWidth(0.8f).padding(start = 10.dp).padding(end = 5.dp).height(120.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.Transparent,
                        focusedContainerColor = Color.White.copy(0.1f),
                        unfocusedContainerColor = Color.White.copy(0.1f),
                        focusedLabelColor = Color.White,
                        focusedIndicatorColor = Color.White.copy(0.5f),
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedLeadingIconColor = Color.White,
                        focusedLeadingIconColor = Color.White,

                        ),
                    shape = RoundedCornerShape(40.dp),
                    textStyle = TextStyle(
                        fontFamily = regular_font,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.width(2.dp))

                Button(
                    onClick = {
                        if (message.isNotEmpty()) {
                            vm.generateTasks(message)
                            message=""
                        }
                    },
                    modifier = modifier
                        .height(120.dp)
//                        .weight(1f)
                        .padding(end =10.dp),         // square button (nice for icons)
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(0.3f),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Send",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    )
    {innerPadding->
        if (showBottomSheet&&selectedTask!=null) {
            TaskDrawer(
                selectedTask!!,
                {selectedTask=null
                showBottomSheet=false},
                markDone = {taskid->
                    vm.removeTask(taskid)
                    selectedTask=null
                    showBottomSheet=false
                }
            )
        }

        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(tasks.size) {
                    TaskItem(tasks[it], {task->
                        selectedTask = task
                        showBottomSheet=true
                    })
                }
            }

        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun MainScreenPrev() {
//    MainScreen(MainViewModel(FirebaseManager(), rememberNavController()))
}