package com.spedison.helper

import java.sql.Connection
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Types


object ShowCols {

    fun print(sql: String, conn: Connection, asConfigFile: Boolean) {
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        this.print(rs, asConfigFile)
        rs.close()
    }

    fun print(res: ResultSet, asConfigFile: Boolean) {
        val rsmd: ResultSetMetaData = res.metaData

        // Print Header
        if (asConfigFile)
            println("##ExcelCollumName,DBColumnName,type,ExcelColumSize,DBColumnSize")
        else
            println("CollumName,DisplayName,Name of Type,ClassName of Column,Label Size")

        // Loop in all columns.
        for (i in 1..rsmd.columnCount) {
            val name = rsmd.getColumnName(i)
            val nameDisplay = rsmd.getColumnLabel(i)
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
            if (asConfigFile)
                println("${name},${name},${nameOfType},${rsmd.getColumnDisplaySize(i) * 100},${rsmd.getColumnDisplaySize(i)}")
            else
                println("${name},${nameDisplay},${nameOfType},${rsmd.getColumnClassName(i)},${rsmd.getColumnDisplaySize(i)}")
        }
    }
}