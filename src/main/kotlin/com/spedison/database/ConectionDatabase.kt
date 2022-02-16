package com.spedison.database

import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class ConectionDatabase(val fileConetion: String) {

    val file = File(fileConetion)
    val prop = Properties()

    fun load(): Unit {
        FileInputStream(file).use {
            prop.load(it)
            it.close()
        }
    }

    fun getConection() : Connection {

        // Somente para carregar o Driver pedido.
        val driver = prop.getProperty("driver").trim()
        Class.forName(driver)

        val connection = DriverManager.getConnection(
            prop.getProperty("url"),
            prop.getProperty("user"),
            prop.getProperty("pass"))

        return connection
    }
}