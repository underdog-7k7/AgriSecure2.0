package com.personal.animeshpandey.agrisecure.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.bluetooth.BluetoothAdapter
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.personal.animeshpandey.agrisecure.Data.bluetooth.ConnectionState
import com.personal.animeshpandey.agrisecure.Data.bluetooth.FarmNotification
import com.personal.animeshpandey.agrisecure.presentation.permissions.PermissionUtils
import com.personal.animeshpandey.agrisecure.presentation.permissions.SystemBroadcastReceiver
import com.personal.animeshpandey.agrisecure.ui.theme.back1
import com.personal.animeshpandey.agrisecure.ui.theme.back4

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(onBluetoothStateChanged: () -> Unit,viewModel:SensorDataViewModel = hiltViewModel(),navController: NavController){

    val scroll = rememberScrollState()

    SystemBroadcastReceiver(systemAction = BluetoothAdapter.ACTION_STATE_CHANGED){ bluetoothState ->
        val action = bluetoothState?.action ?: return@SystemBroadcastReceiver
        if(action == BluetoothAdapter.ACTION_STATE_CHANGED){
            onBluetoothStateChanged()
        }
    }

    val permissionState = rememberMultiplePermissionsState(permissions = PermissionUtils.permissions)
    val lifecycleOwner = LocalLifecycleOwner.current
    val bleConnectionState = viewModel.connectionState

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver{_,event ->
                if(event == Lifecycle.Event.ON_START){
                    permissionState.launchMultiplePermissionRequest()
                    if(permissionState.allPermissionsGranted && bleConnectionState == ConnectionState.Disconnected){
                        viewModel.reconnect()
                    }
                }
                if(event == Lifecycle.Event.ON_STOP){
                    if (bleConnectionState == ConnectionState.Connected){
                        viewModel.disconnect()
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    LaunchedEffect(key1 = permissionState.allPermissionsGranted){
        if(permissionState.allPermissionsGranted){
            if(bleConnectionState == ConnectionState.Uninitialized){
                viewModel.initializeConnection()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()){

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)) {
            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "Hi Animesh!", fontSize = 64.sp, color = back4)
            }
            Row(modifier = Modifier.fillMaxWidth()){
                if(bleConnectionState == ConnectionState.CurrentlyInitializing){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), shape = CardDefaults.elevatedShape, colors = CardDefaults.cardColors(contentColor = Color.White, containerColor = back4)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){

                                CircularProgressIndicator(modifier = Modifier.padding(8.dp), color = back4, trackColor = Color.White)
                                if(viewModel.initializingMessage != null){
                                    Text(
                                        text = viewModel.initializingMessage!!
                                    )
                                }
                            }
                        }

                        AnimatedWeatherCard(temperature = "Fetching", pressure = "Fetching", rainfall = "Fetching", soil = "Fetching")
                    }

                }
                else if(!permissionState.allPermissionsGranted){
                    CircularProgressIndicator(modifier = Modifier.padding(8.dp), color = Color.White, trackColor = Color.Red)
                    Text(
                        text = "Please enable the missing permissions to continue",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                else if(viewModel.errorMessage != null){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedWeatherCard(temperature = "Fetching", pressure = "Fetching", rainfall = "Fetching", soil = "Fetching")
                        Text(
                            text = viewModel.errorMessage!!
                        )
                        Button(
                            onClick = {
                                if(permissionState.allPermissionsGranted){
                                    viewModel.initializeConnection()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = back4, contentColor = Color.White)
                        ) {
                            Text(text = "Try again")
                        }
                    }
                }
                else if(bleConnectionState == ConnectionState.Connected){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), shape = CardDefaults.elevatedShape, colors = CardDefaults.cardColors(contentColor = Color.White, containerColor = back4)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                                Text(
                                        text = "Connected to Farm Module"
                                    )
                            }
                        }

                        AnimatedWeatherCard(temperature = "Fetching", pressure = "Fetching", rainfall = "Fetching", soil = "Fetching")
                    }
                }

                else if(bleConnectionState == ConnectionState.Disconnected){
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        AnimatedWeatherCard(temperature = "Fetching", pressure = "Fetching", rainfall = "Fetching", soil = "Fetching")
                        Button(onClick = {
                            viewModel.initializeConnection()
                        },
                            colors = ButtonDefaults.buttonColors(containerColor = back4, contentColor = Color.White)) {
                            Text("Initialize again")
                        }

                    }

                }
            }
            FarmNotificationCard(
                notification = FarmNotification(
                    id = 1,
                    title = "Sprinkler Activation",
                    message = "Sprinkler for 15 minutes.",
                    timestamp = "12:32 AM" // Random time within the last 24 hours
                )
            )
            FarmNotificationCard(
                FarmNotification(
                    id = 2,
                    title = "Fertilizer overdose detected",
                    message = "N02 found beyond limits",
                    timestamp = "1:00 PM" // Random time within the last 24 hours
                )
            )
        }

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
