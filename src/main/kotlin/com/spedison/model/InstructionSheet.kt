package com.spedison.model

import java.io.File
import java.io.FileInputStream
import java.util.*

class InstructionSheet() {

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
        val f: File = File(fileName)
        if (!f.exists())
            return false

        FileInputStream(f).use {
            prop.load(it)
            this.text = prop.getProperty("Text", "Without text")
            this.sheetName = prop.getProperty("SheetName", "SheetName withou name")
            this.row = prop.getProperty("Row", "0").toInt()
            this.col = prop.getProperty("Col", "0").toInt()
            this.width = prop.getProperty("Width", "1000").toInt()
            this.height = prop.getProperty("Height", "1000").toInt()
        }

        return true
    }


}