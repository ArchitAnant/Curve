package com.ari.curve

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ari.curve.data.FirebaseManager
import com.ari.curve.data.GeminiRepo
import com.ari.curve.ui.naviagtions.NavGraph
import com.ari.curve.ui.screens.MainScreen
import com.ari.curve.ui.theme.CurveTheme
import com.ari.curve.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurveTheme {
                val navHostController = rememberNavController()
                val firebaseManager = FirebaseManager()
                val geminiRepo = GeminiRepo()
                val main_vm = MainViewModel(firebaseManager,navHostController,geminiRepo)
                Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Black) { innerPadding ->

                    NavGraph(this,navHostController,main_vm,modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurveTheme {
        Greeting("Android")
    }
}