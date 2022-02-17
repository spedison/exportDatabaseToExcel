package com.spedison.helper

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone
import java.util.stream.Collectors

object ListTimeZone {

    fun print() {
        println("Current Timezone : [" + System.getProperty("user.timezone") + "]")
        val tmz = TimeZone.getTimeZone(System.getProperty("user.timezone"))
        println("Timezone Display Name : " + tmz.getDisplayName())

        val instant: Instant = Instant.now()
        println("Now is ${instant.atZone(tmz.toZoneId())}")
        println("==============================================")
        ZoneId.getAvailableZoneIds()
            .stream()
            .sorted()
            .forEach { strZoneId: String? ->
                System.out.printf("%-35s: %-6s%n",
                    strZoneId, getTzOffsetString(ZoneId.of(strZoneId), instant))
            }
    }

    fun getTimeZoneId(id: String = System.getProperty("user.timezone")): TimeZone =
        ZoneId.getAvailableZoneIds()
            .stream()
            .filter({ str -> str.trim().lowercase().equals(id.trim().lowercase()) })
            .filter({ str -> str != null })
            .map { strZoneId: String ->
                TimeZone.getTimeZone(strZoneId)
            }.collect(Collectors.toList())[0]

    fun getTzOffsetString(zoneId: ZoneId?, instant: Instant?): String {
        return ZonedDateTime.ofInstant(instant, zoneId).offset.toString()
    }



}