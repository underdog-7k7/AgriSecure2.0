package com.personal.animeshpandey.agrisecure.presentation

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.personal.animeshpandey.agrisecure.ui.theme.back3
import com.personal.animeshpandey.agrisecure.ui.theme.back4



@Composable
fun ContractScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){
        contractsummary()
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

@Composable
fun contractsummary(){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Contract Summary",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 32.sp,
            color = back4
        )

        ContractItem("Coverage:", "Risks - Pests, Adverse Weather", "Limits - $10,000 per acre", "Deductibles - $500")
        Spacer(modifier = Modifier.height(8.dp))
        ContractItem("Premium:", "Total - 1700₹", "Schedule - Semi Yearly")
        Spacer(modifier = Modifier.height(8.dp))
        ContractItem("Triggers:", "Thresholds - 20% yield loss", "Documentation - Yield records, Weather reports")
        Spacer(modifier = Modifier.height(8.dp))
        ContractItem("Smart Contract:", "Address - ", "Code - 0x456def", "Terms - Automated claim processing")
        Spacer(modifier = Modifier.height(8.dp))
        ContractItem("Contact:", "Provider - , 123-456-7890", "Channels - Online portal, Email")
    }
}

@Composable
fun ContractItem(title: String, vararg details: String) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = back4, contentColor = Color.White)) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(8.dp),
                fontSize = 24.sp,

                )
            Column {
                details.forEach { detail ->
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 16.sp

                    )
                }
            }
        }
    }

}