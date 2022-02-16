package com.spedison.model

import com.spedison.model.enuns.TypeColumn
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class ColumnsDatabaseTest() {

    val fileName = "/home/edisonribeiroaraujo/Documentos/codigos-ensaio/Apache-POI-Excel/conf/coluns.config.csv"

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun readConfiguration() {

        val cols : ColumnsDatabase = ColumnsDatabase()
        cols.readConfiguration(listOf<String> ("Identificador,id,int,0,100"))
        assertEquals(cols[0].nameExcel,"Identificador")
        assertEquals(cols[0].nameDatabase,"id")
        assertEquals(cols[0].lengthColumnDatabase,0)
        assertEquals(cols[0].lengthExcel,100)
        assertEquals(cols[0].type,TypeColumn.INT)

    }

    @Test
    fun readFileConfiguration() {

        val cols : ColumnsDatabase = ColumnsDatabase()
        cols.readFileConfiguration(File(fileName))
        assertEquals(cols[0].nameExcel,"Identificador")
        assertEquals(cols[0].nameDatabase,"id")
        assertEquals(cols[0].lengthColumnDatabase,0)
        assertEquals(cols[0].lengthExcel,100)
        assertEquals(cols[0].type,TypeColumn.INT)

        assertEquals(cols[1].nameExcel,"Nome_Do_Campo")
        assertEquals(cols[1].nameDatabase,"nomecampo")
        assertEquals(cols[1].lengthColumnDatabase,200)
        assertEquals(cols[1].lengthExcel,200)
        assertEquals(cols[1].type,TypeColumn.STRING)

    }
}