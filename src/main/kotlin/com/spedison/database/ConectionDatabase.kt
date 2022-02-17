package com.spedison.database

import java.io.Console
import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class ConectionDatabase(private val fileConetion: String) {

    private var file: File
    private val prop = Properties()

    init {
        file = File(fileConetion)
    }

    fun load() {
        FileInputStream(file).use {
            prop.load(it)
            it.close()
        }

        if (prop.getProperty("user").trim().startsWith("***")) {
            print("Inform user database : ")
            val user: String = readln()
            prop.setProperty("user", user)
            println()
        }

        if (prop.getProperty("pass").trim().startsWith("***")) {
            val console: Console? = System.console()
            when (console) {
                null -> {
                    println("Console Terminal does not exist. Use \"pass\" parameter in [connection.properties] file.")
                    System.exit(1)
                }
                else -> {
                    val pass = console.readPassword("Inform user password :")
                    prop.setProperty("pass", String(pass))
                    // Empty char array for security reasons
                    for (i in 0 until pass.size)
                        pass[i] = ' '
                    println()
                }
            }
        }
    }

    fun getConection(): Connection {

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