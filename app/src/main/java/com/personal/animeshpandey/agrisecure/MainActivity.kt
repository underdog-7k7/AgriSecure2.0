package com.personal.animeshpandey.agrisecure

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.personal.animeshpandey.agrisecure.presentation.AirQualityViewModel
import com.personal.animeshpandey.agrisecure.presentation.Login
import com.personal.animeshpandey.agrisecure.presentation.Navigation
import com.personal.animeshpandey.agrisecure.ui.theme.AgriSecureTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val airQualityViewModel:AirQualityViewModel = viewModel()
            AgriSecureTheme {
                Navigation(
                    onBluetoothStateChanged = {
                        showBluetoothDialog()
                    },airQualityViewModel = airQualityViewModel
                )
            }
        }
    }


    override fun onStart() {
        super.onStart()
        showBluetoothDialog()
    }


    private var isBluetootDialogAlreadyShown = false
    private fun showBluetoothDialog(){
        if(!bluetoothAdapter.isEnabled){
            if(!isBluetootDialogAlreadyShown){
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startBluetoothIntentForResult.launch(enableBluetoothIntent)
                isBluetootDialogAlreadyShown = true
            }
        }
    }

    private val startBluetoothIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            isBluetootDialogAlreadyShown = false
            if(result.resultCode != Activity.RESULT_OK){
                showBluetoothDialog()
            }
        }
}
