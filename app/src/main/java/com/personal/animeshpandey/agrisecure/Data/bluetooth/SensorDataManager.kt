package com.personal.animeshpandey.agrisecure.Data.bluetooth

import com.personal.animeshpandey.agrisecure.Util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface SensorDataManager {
    val data: MutableSharedFlow<Resource<ReadingModel>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()
}