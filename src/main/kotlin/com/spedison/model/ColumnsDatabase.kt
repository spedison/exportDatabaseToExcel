package com.spedison.model

import com.spedison.model.enuns.TypeColumn
import java.io.File
import java.util.LinkedList

class ColumnsDatabase : LinkedList<ColumnDatabase>() {

    fun readConfiguration(lines: List<String>) {
        lines
            .filter { line -> !line.trim().startsWith("##") }
            .filter { line -> !line.trim().startsWith("--") }
            .filter { line -> !line.trim().isEmpty() }
            .map { s -> s.split(",") }
            .map { sArray ->
                ColumnDatabase(
                    sArray[0],
                    sArray[1],
                    TypeColumn.convertFromString(sArray[2]),
                    sArray[4].toInt(),
                    sArray[3].toInt()
                )
            }
            .forEach(this::add)
    }

    fun readFileConfiguration(file: File) {
        readConfiguration(file.readLines())
    }
}