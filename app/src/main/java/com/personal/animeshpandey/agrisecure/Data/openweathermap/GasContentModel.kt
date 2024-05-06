package com.personal.animeshpandey.agrisecure.Data.openweathermap

data class Response(
    val coords: Coordinate?,
    val list: List<GasData>?
)

data class Coordinate(
    val lon:Int,
    val lat:Int
)

data class GasData(
    val main: AQIContainer,
    val components: GasTypeData,
    val dt: Long
)

data class AQIContainer(
    val AQI: Int
)

data class GasTypeData(
    val co: Float,
    val no: Float,
    val n02: Float,
    val o3: Float,
    val so2 : Float,
    val pm2_5: Float,
    val pm10: Float,
    val nh3: Float
)