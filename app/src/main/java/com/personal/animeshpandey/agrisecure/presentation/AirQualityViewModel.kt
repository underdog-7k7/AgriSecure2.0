package com.personal.animeshpandey.agrisecure.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.animeshpandey.agrisecure.Data.openweathermap.Response
import com.personal.animeshpandey.agrisecure.Data.openweathermap.airqualityservice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject



class AirQualityViewModel :ViewModel(){

    private val _datastate = mutableStateOf(currentstate())
    val exposeddatastate: State<currentstate>  =_datastate

    init{
        Log.d("GENESIS VM","VM Created")
        getAirQuality()
    }

    fun getAirQuality(){
        Log.d("GENESIS VM AIR QUALITY FETCH","VM Created fetching started")
        viewModelScope.launch{
        Log.d("DONE IN SCOPE","SCOPE LAUNCH SUCCESSFUL")
            try{
                Log.d("TRYING FOR RESPONSE","LETS SEE")
                val response = airqualityservice.fetchAirQuality()
                Log.d("Response Obtained",response.toString())
                _datastate.value = _datastate.value.copy(
                    loading = false,
                    error = null,
                    recived_data = response
                )
                Log.d("Response created", exposeddatastate.toString())
            }
            catch (e:Exception){
                Log.d("RESPONSE FORMAT ERROR", e.toString())
                _datastate.value = _datastate.value.copy(
                    recived_data = Response(null,null),
                        loading = false,
                        error = "Endpoint error!"
                )
            }
        }
    }
}

data class currentstate(
    val loading:Boolean = true,
    val recived_data: Response = Response(null,null),
    val error:String? = null
)