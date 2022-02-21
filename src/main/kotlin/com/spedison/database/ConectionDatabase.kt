package com.spedison.database

import java.io.Console
import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class ConectionDatabase {

    private var file: File = File("")
    private val prop = Properties()
    private var fileConetion: String = ""
    private var verbose: Boolean = false

    fun setVerbose(verbose: Boolean) {
        this.verbose = verbose
    }

    fun load(fileConetion: String) {
        this.fileConetion = fileConetion
        this.file = File(fileConetion)

        if (!file.exists()) {
            if (verbose){
                println("File ${fileConetion} is not exists")
            }
            return
        }

        FileInputStream(file).use {
            prop.load(it)
            it.close()
            if (verbose){
                println("File ${fileConetion} was loaded")
            }
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

        if (verbose)
            println("The class ${driver} was loaded.")

        val connection = DriverManager.getConnection(
            prop.getProperty("url"),
            prop.getProperty("user"),
            prop.getProperty("pass"))

        if (verbose)
            println("The connection was created.")

        return connection
    }
}