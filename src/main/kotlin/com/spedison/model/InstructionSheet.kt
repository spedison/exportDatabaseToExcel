package com.spedison.model

import java.io.BufferedReader
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Files.*
import java.nio.file.Paths
import java.nio.file.Paths.*
import java.util.*


class InstructionSheet(private val verbose: Boolean = false) {

    var text: String = ""
        private set

    var sheetName: String = ""
        private set

    var row: Int = 0
        private set

    var col: Int = 0
        private set

    var width: Int = 0
        private set

    var height: Int = 0
        private set


    fun loadFromFile(fileName: String): Boolean {

        val prop = Properties()
        val f = File(fileName)
        if (!f.exists()) {
            if (verbose) {
                println("${fileName} is not exists. Do not load.")
            }
            return false
        }
        val buffReader: BufferedReader = newBufferedReader(get(fileName), Charset.defaultCharset())

        //FileInputStream(f).use {
        prop.load(buffReader)
        this.text = prop.getProperty("Text", "Without text")
        this.sheetName = prop.getProperty("SheetName", "SheetName withou name")
        this.row = prop.getProperty("Row", "0").toInt()
        this.col = prop.getProperty("Col", "0").toInt()
        this.width = prop.getProperty("Width", "1000").toInt()
        this.height = prop.getProperty("Height", "1000").toInt()

        if (verbose)
            println("Instruction Sheet data was loaded.")

        buffReader.close()
        //}

        return true
    }

}