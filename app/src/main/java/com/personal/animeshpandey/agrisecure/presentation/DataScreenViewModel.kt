package com.personal.animeshpandey.agrisecure.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.animeshpandey.agrisecure.Data.bluetooth.ConnectionState
import com.personal.animeshpandey.agrisecure.Data.bluetooth.SensorDataManager
import com.personal.animeshpandey.agrisecure.Util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorDataViewModel @Inject constructor(
    private val SensorDataRecieveManager: SensorDataManager
) : ViewModel(){

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var temperature by mutableStateOf(-3)
        private set

    var humidity by mutableStateOf(-3)
        private set

    var rain by mutableStateOf(-3)
        private set

    var soil by mutableStateOf(-3)
        private set

    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges(){
        viewModelScope.launch {
            SensorDataRecieveManager.data.collect{ result ->
                when(result){
                    is Resource.Success -> {
                        connectionState = result.data.ConnectionState
                        temperature = result.data.temparature
                        humidity = result.data.humidity
                        rain = result.data.RainState
                        soil = result.data.SoilHydrogenics
                        Log.d("BLUETOOTH DATA", rain.toString())
                    }

                    is Resource.Loading -> {
                        initializingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun disconnect(){
        SensorDataRecieveManager.disconnect()
    }

    fun reconnect(){
        SensorDataRecieveManager.reconnect()
    }

    fun initializeConnection(){
        errorMessage = null
        subscribeToChanges()
        SensorDataRecieveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        SensorDataRecieveManager.closeConnection()
    }


}
