package com.ari.curve.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ari.curve.R
import com.ari.curve.regular_font

@Composable
fun SignInScreen(onSignInClick: () -> Unit,modifier: Modifier = Modifier) {
    Scaffold (
        containerColor = Color.Black

        ){innerPadding->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(innerPadding)
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .fillMaxSize()
                    .padding(vertical = 25.dp)
        ) {
                    Text(
                        text = "Sign in with Google",
                        color = Color.White,
                        fontFamily = regular_font,
                        fontSize = 18.sp,
                        modifier = modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = {
                            onSignInClick()
                        },
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Image(
                            painterResource(R.drawable.android_dark_rd_si),
                            contentDescription = null,
                            modifier = Modifier.width(150.dp)
                        )
                    }
                }
        }
}

@Preview
@Composable
private fun SignInScreenPrev() {
    SignInScreen({})
}