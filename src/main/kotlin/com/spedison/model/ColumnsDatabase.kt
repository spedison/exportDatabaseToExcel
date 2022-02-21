package com.spedison.model

import com.spedison.model.enuns.TypeColumn
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.sql.Connection
import java.sql.ResultSetMetaData
import java.sql.Types
import java.util.LinkedList
import kotlin.math.min

class ColumnsDatabase : LinkedList<ColumnDatabase>() {

    fun readConfiguration(lines: List<String>) {
        lines
            .filter { line -> !line.trim().startsWith("##") }
            .filter { line -> !line.trim().startsWith("--") }
            .filter { line -> !line.trim().isEmpty() }
            .map { s -> s.split(",") }.map { sArray ->
                ColumnDatabase(sArray[0],
                    sArray[1],
                    TypeColumn.convertFromString(sArray[2]),
                    sArray[4].toInt(),
                    sArray[3].toInt())
            }.forEach(this::add)
    }

    fun readFileConfiguration(file: File) {
        readConfiguration(file.readLines())
    }

    fun writeConfiguration(file: File) {
        val out: OutputStream = FileOutputStream(file)
        out.write("##ExcelName,DBName,Type,LengthExcel,LengthDB\n".toByteArray(Charset.forName("UTF-8")))
        this.stream().map(ColumnDatabase::toString).map { it -> it + "\n" }
            .forEach { it -> out.write(it.toByteArray(Charset.forName("UTF-8"))) }
        out.close()
    }

    fun getColumns(sql: String, conn: Connection) {

        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        val rsmd: ResultSetMetaData = rs.metaData

        this.clear()

        // Loop in all columns.
        for (i in 1..rsmd.columnCount) {
            val name = rsmd.getColumnName(i)
            val labelName = rsmd.getColumnLabel(i)
            val nameOfType: String = when (rsmd.getColumnType(i)) {
                Types.BIT -> "int"
                Types.INTEGER -> "int"
                Types.BIGINT -> "long"
                Types.ARRAY -> "Array-NS"
                Types.BOOLEAN -> "int"
                Types.CHAR -> "str"
                Types.DATALINK -> "Datalink-NS"
                Types.DECIMAL -> "double"
                Types.CLOB -> "CLob-NS"
                Types.BLOB -> "BLob-NS"
                Types.DATE -> "date"
                Types.TIME -> "time"
                Types.TIMESTAMP -> "timestamp"
                Types.TIMESTAMP_WITH_TIMEZONE -> "timestamp"
                Types.DISTINCT -> "Distinct-NS"
                Types.VARCHAR -> "str"
                Types.VARBINARY -> "Var Binary-NS"
                Types.LONGNVARCHAR -> "str"
                else -> "Unknown Type-NS"
            }

            val typeColumn: TypeColumn = TypeColumn.convertFromString(nameOfType)
            this.add(ColumnDatabase(labelName,
                name,
                typeColumn,
                rsmd.getColumnDisplaySize(i),
                min(rsmd.getColumnDisplaySize(i) * 50, 250 * 250)))
        }

        rs.close()
    }

}