package com.spedison.helper

import com.spedison.model.ColumnsDatabase
import com.spedison.model.ColumnDatabase
import krangl.DataFrame
import krangl.DataFrameRow
import krangl.dataFrameOf
import krangl.fromResultSet
import java.sql.Connection
import java.sql.Date


object DataFrameHelper {

    fun createDataFrameFromQuery(conn:Connection, sql:String): DataFrame {
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        val ret = DataFrame.fromResultSet(rs)
        // rs.close()
        // stmt.close()
        return ret
    }

    fun readInt(line:Int,  columnsDatabase: ColumnDatabase, data:DataFrame): Int {
        val columName : String = columnsDatabase.nameDatabase
        val row : DataFrameRow = data.row(line)
        val ret : Any? = row.get(columName.lowercase())
        if ( ret is Long)
            return ret.toInt()
        if ( ret is Int)
            return ret
        throw RuntimeException("Type Mismath")
    }

    fun readLong(line:Int, columnsDatabase: ColumnDatabase, data:DataFrame): Long {
        val columName : String = columnsDatabase.nameDatabase
        val row : DataFrameRow = data.row(line)
        val ret : Any? = row.get(columName.lowercase())

        if ( ret is Long)
            return ret

        if ( ret is Int)
            return ret.toLong()

        throw RuntimeException("Type Mismath")
    }

    fun readDouble(line:Int, columnsDatabase: ColumnDatabase, data:DataFrame): Double {
        val columName : String = columnsDatabase.nameDatabase
        val row : DataFrameRow = data.row(line)
        val ret : Any? = row.get(columName.lowercase())

        if ( ret is Double)
            return ret
        if ( ret is Float)
            return ret.toDouble()
        throw RuntimeException("Type Mismath")
    }

    fun readFloat(line:Int, columnsDatabase: ColumnDatabase, data:DataFrame): Float {
        val columName : String = columnsDatabase.nameDatabase
        val row : DataFrameRow = data.row(line)
        val ret : Any? = row.get(columName.lowercase())

        if ( ret is Float)
            return ret
        if ( ret is Double)
            return ret.toFloat()

        throw RuntimeException("Type Mismath")
    }

    fun readString(line:Int, columnsDatabase: ColumnDatabase, data:DataFrame): String {
        val columName : String = columnsDatabase.nameDatabase
        val row : DataFrameRow = data.row(line)
        return row.get(columName.lowercase()) as String
    }

    fun isNull(line:Int, columnsDatabase: ColumnDatabase, data:DataFrame): Boolean {
        val columName : String = columnsDatabase.nameDatabase
        val row : DataFrameRow = data.row(line)
        return (row.get(columName.lowercase()) == null)
    }


    fun readDate(line:Int, columnsDatabase: ColumnDatabase, data:DataFrame): java.util.Date {
        val columName : String = columnsDatabase.nameDatabase
        val row : DataFrameRow = data.row(line)
        return java.util.Date ((row.get(columName) as java.sql.Date).toInstant().toEpochMilli())
    }



        /*
        // convert into DataFrame
        val fromResultSet: DataFrame = DataFrame.fromResultSet(rs)

        val ret : DataFrame = dataFrameOf(listOf("Col1", "Col2").iterator())(0,0)

        val df: DataFrame = dataFrameOf(
            listOf("first_name", "last_name", "age", "weight"))()

        val cols = columns.map { ColumnDatabase::nameDatabase }

        return dataFrameOf(columns.map(ColumnDatabase::nameDatabase))*/

}