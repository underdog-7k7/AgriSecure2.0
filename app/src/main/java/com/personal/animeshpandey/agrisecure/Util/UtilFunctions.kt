package com.personal.animeshpandey.agrisecure.Util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun unixTimestampToIndiaTimeZone(unixTimestamp: Long?):String{
    if(unixTimestamp==null) return "Could not fetch"
    val utcDateTime = LocalDateTime.ofEpochSecond(unixTimestamp, 0, ZoneOffset.UTC)
    val indiaTimeZone = ZoneId.of("Asia/Kolkata")
    val time = utcDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(indiaTimeZone).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd ")
    return time.format(formatter).toString()
}

fun main(){
    println(unixTimestampToIndiaTimeZone(1714991235))
}