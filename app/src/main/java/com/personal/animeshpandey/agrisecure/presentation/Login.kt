package com.personal.animeshpandey.agrisecure.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.personal.animeshpandey.agrisecure.R
import com.personal.animeshpandey.agrisecure.ui.theme.back2
import com.personal.animeshpandey.agrisecure.ui.theme.back4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController) {

//    val signup_uri = ""

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember{mutableStateOf(false)}

    val context = LocalContext.current

    // Animation for logo
    val scaleAnimation = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        scaleAnimation.animateTo(
            targetValue = 1.4f,
            animationSpec = tween(durationMillis = 2000)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Image with animation
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = back4), horizontalArrangement = Arrangement.End) {
            val scale by scaleAnimation.asState()
            Image(
                painter = painterResource(id = R.drawable.icon1),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(350.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            )
        }

        Spacer(modifier = Modifier
            .height(32.dp)
            .background(color = Color.White, shape = MaterialTheme.shapes.extraLarge))

        Column(verticalArrangement = Arrangement.SpaceAround) {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 32.dp)
                        .animateContentSize(), // Animate size changes
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                    shape = MaterialTheme.shapes.large,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = back4,
                        unfocusedBorderColor = back2
                    ),

                    )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 32.dp)
                        .animateContentSize(), // Animate size changes
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                    shape = MaterialTheme.shapes.large,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = back4,
                        unfocusedBorderColor = back2
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            var bothfilled = false

            if (email != "" && password != "") {
                bothfilled = true
            } else { false }

            val buttonOpacity by animateFloatAsState(
                targetValue = if (bothfilled) 1f else 0.5f, // Animate opacity based on fields filled
                animationSpec = tween(durationMillis = 300)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        if(email=="animeshpandey98@yahoo.com" && password=="some_password"){
                            navController.navigate(Screen.MainScreen.route){
                                popUpTo(Screen.MainScreen.route){
                                    inclusive= true
                                }
                            }
                        }
                        else{
                            showMessage(context,"Invalid Id or Password")
                              }}, //implement login and nav
                    enabled = bothfilled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = back4,
                        disabledContainerColor = Color.LightGray
                    ),
                    modifier = Modifier.alpha(buttonOpacity) // Apply animated opacity
                ) {
                    Text("Submit", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text("Don't have an account?",color = back4)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Sign Up Options
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sign up using ",color = back4)
                    IconButton(onClick = { /* Handle Google Sign Up */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Google Icon")
                    }
                    IconButton(onClick = { /* Handle Phone Number Sign Up */ }) {
                        Icon(Icons.Default.Phone, contentDescription = "Phone Icon")
                    }
                    IconButton(onClick = { /* Handle Manual Sign Up */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Phone Icon")
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom){
                Text(modifier = Modifier
                    .padding(12.dp)
                    .clickable { }, text = "Reset Password", color = Color.Red, fontSize = 18.sp)

            }


        }
    }}

fun showMessage(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
