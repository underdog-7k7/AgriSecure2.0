package com.personal.animeshpandey.agrisecure.presentation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.animeshpandey.agrisecure.Data.bluetooth.FarmNotification
import com.personal.animeshpandey.agrisecure.Util.unixTimestampToIndiaTimeZone
import com.personal.animeshpandey.agrisecure.ui.theme.back1
import com.personal.animeshpandey.agrisecure.ui.theme.back2
import com.personal.animeshpandey.agrisecure.ui.theme.back3
import com.personal.animeshpandey.agrisecure.ui.theme.back4
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnimatedWeatherCard(
    temperature: String,
    pressure: String,
    rainfall: String,
    soil: String,
    airQualityViewModel: AirQualityViewModel
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val infiniteTransition = rememberInfiniteTransition()
    val elevation by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = back4, contentColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text(modifier = Modifier.padding(bottom = 12.dp), text = "General Metrics", color = Color.White, fontSize = 24.sp)
            }
            Text(text = "Temperature: $temperature")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Pressure: $pressure")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Rainfall: $rainfall")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Soil: $soil")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button( onClick = {expanded=!expanded},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = back4)//Show Description
                ){
                    if(expanded==true){
                        Icon(modifier = Modifier.size(24.dp), imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "showmore",)
                    }else{
                        Icon(modifier = Modifier.size(24.dp), imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "showmore")
                    }
                }
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(16.dp)),){
                AnimatedVisibility(visible = expanded) {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Card(modifier = Modifier.padding(4.dp), colors = CardDefaults.cardColors(containerColor = back4, contentColor = Color.White), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(8.dp)) {
                            Text(modifier = Modifier.padding(8.dp), text = "Other Metrics", color = Color.White, fontSize = 16.sp, fontStyle = FontStyle.Normal)
                        }
                    }
                    Spacer(modifier = Modifier.height(64.dp))

                    if(airQualityViewModel.exposeddatastate.value.loading==false &&
                        airQualityViewModel.exposeddatastate.value.error.toString()=="null"){
                        val co = airQualityViewModel.exposeddatastate.value.recived_data.list?.get(0)?.components?.co.toString()
                        val no = airQualityViewModel.exposeddatastate.value.recived_data.list?.get(0)?.components?.no.toString()
                        val n02 = airQualityViewModel.exposeddatastate.value.recived_data.list?.get(0)?.components?.n02.toString()
                        val o3 = airQualityViewModel.exposeddatastate.value.recived_data.list?.get(0)?.components?.o3.toString()
                        val so2 = airQualityViewModel.exposeddatastate.value.recived_data.list?.get(0)?.components?.so2.toString()
                        val nh3 = airQualityViewModel.exposeddatastate.value.recived_data.list?.get(0)?.components?.nh3.toString()
                        val timestamp = airQualityViewModel.exposeddatastate.value.recived_data.list?.get(0)?.dt

                        val datetime = unixTimestampToIndiaTimeZone(timestamp)
                        
                        Column(modifier = Modifier.padding(top = 48.dp, start = 8.dp, end = 8.dp),) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Carbon Monoxide: $co", color = back4)
                            Text("Nitrogen Oxide: $no",color = back4)
                            Text("Nitrogen Dioxide: $n02",color = back4)
                            Text("Ozone: $o3",color = back4)
                            Text("Sulphur Dioxide $so2",color = back4)
                            Text("Ammonia: $nh3",color = back4)
                            Text("Last Updated: $datetime", color = back4)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                Button(onClick = {
                                    airQualityViewModel.getAirQuality()
                                },
                                    colors = ButtonDefaults.buttonColors(containerColor = back4, contentColor = Color.White)) {
                                    Icon(Icons.Filled.Refresh, contentDescription = null)
                                    Text(text = "Refresh")
                                }
                            }
                        }
                    }
                    else if(airQualityViewModel.exposeddatastate.value.loading==false &&
                        airQualityViewModel.exposeddatastate.value.error==null){
                        Text(text = "Error Fetching Data")
                    }
                    else if(airQualityViewModel.exposeddatastate.value.loading==true){
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun FarmNotificationList() {
    LazyColumn {
        items(notifications) { notification ->
            FarmNotificationCard(notification = notification)
        }
    }
}

var notifications = listOf<FarmNotification>(
    FarmNotification(
        id = 1,
        title = "Sprinkler Activation",
        message = "Sprinkler in Zone 2 activated for 15 minutes.",
        timestamp = "12:32 AM" // Random time within the last 24 hours
    ),
    FarmNotification(
        id = 2,
        title = "Fertilizer overdose detected",
        message = "N02 found beyond limits",
        timestamp = "1:00 PM" // Random time within the last 24 hours
    )


)

@Composable
fun FarmNotificationCard(notification: FarmNotification) {
    val visibleState = remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visibleState.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = notification.title)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = notification.message)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = notification.timestamp)
            }
        }
    }
}



