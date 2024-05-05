package com.personal.animeshpandey.agrisecure.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.personal.animeshpandey.agrisecure.ui.theme.back2
import com.personal.animeshpandey.agrisecure.ui.theme.back4
import coil.compose.rememberImagePainter
import com.personal.animeshpandey.agrisecure.ui.theme.back1

@Composable
fun RaiseClaim(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){
        InsuranceClaim()
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(color = back4), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
            Button(onClick = {
                navController.navigate(Screen.ContractScreen.route)
            },
                colors = ButtonDefaults.buttonColors(containerColor = back4)) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.White)
            }
            Button(onClick = {navController.navigate(Screen.MainScreen.route) },
                colors = ButtonDefaults.buttonColors(containerColor = back4)) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
            }
            Button(onClick = { navController.navigate(Screen.RaiseClaimScreen.route) },
                colors = ButtonDefaults.buttonColors(containerColor = back4)) {
                Icon(Icons.Default.Create, contentDescription = null, tint = Color.White)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsuranceClaim() {
    var claimTitle by remember { mutableStateOf(TextFieldValue("")) }
    var adversaryType by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text("Raise A Claim", color = back4, fontSize = 32.sp)

        androidx.compose.material3.OutlinedTextField(
            value = claimTitle,
            onValueChange = { claimTitle = it },
            label = { androidx.compose.material3.Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .animateContentSize(), // Animate size changes
            leadingIcon = {
                androidx.compose.material3.Icon(
                    Icons.Default.Edit,
                    contentDescription = "Email Icon"
                )
            },
            shape = androidx.compose.material3.MaterialTheme.shapes.large,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = back4,
                unfocusedBorderColor = back2
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Type of Claim", color = back4, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Card(modifier = Modifier.padding(4.dp), colors = CardDefaults.cardColors(contentColor = back4, containerColor = Color.White), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    Text("Adverse weather")
                    RadioButton(
                        selected = adversaryType == "Adverse weather",
                        onClick = { adversaryType = "Adverse weather" },
                        colors = RadioButtonDefaults.colors(selectedColor = back4)
                    )
                }
                Column(modifier = Modifier.padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    Text("Vandalised Crops")
                    RadioButton(
                        selected = adversaryType == "Vandalised Crops",
                        onClick = { adversaryType = "Vandalised Crops" },
                        colors = RadioButtonDefaults.colors(selectedColor = back4)
                    )
                }
                Column(modifier = Modifier.padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    Text("Soil Decay")
                    RadioButton(
                        selected = adversaryType == "Soil Contamination",
                        onClick = { adversaryType = "Soil Contamination" },
                        colors = RadioButtonDefaults.colors(selectedColor = back4)
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        ImageSelector()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            if (imageUri.isNotEmpty()) {
                Image(
                    painter = painterResource(id = imageUri.toInt()),
                    contentDescription = "Uploaded Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            androidx.compose.material3.Button(
                onClick = { },
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = back4,
                    disabledContainerColor = Color.LightGray
                ), shape = RoundedCornerShape(16.dp)
            ) {
                androidx.compose.material3.Text("Raise Claim", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ImageSelector() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        androidx.compose.material3.Button(
            onClick = {pickImageActivityResultLauncher.launch("image/*") },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = back4,
                disabledContainerColor = Color.LightGray
            ), shape = RoundedCornerShape(16.dp)
        ) {
            androidx.compose.material3.Text("Upload Images", fontSize = 16.sp)
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberImagePainter(data = imageUri),
                contentDescription = "Raise Claim",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text("No image selected", modifier = Modifier.align(Alignment.Center))
        }
    }
}
