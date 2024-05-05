package com.personal.animeshpandey.agrisecure.Data.bluetooth

data class ReadingModel(
    val temparature:Int,
    val humidity:Int,
    val SoilHydrogenics:Int,
    val RainState:Int,
    val ConnectionState:ConnectionState
)

data class FarmNotification(
    val id: Int,
    val title: String,
    val message: String,
    val timestamp: String
)