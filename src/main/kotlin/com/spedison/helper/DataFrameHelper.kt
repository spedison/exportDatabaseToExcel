package com.spedison.helper

import com.spedison.model.ColumnDatabase
import com.spedison.model.ColumnsDatabase
import com.spedison.model.enuns.TypeColumn
import krangl.*
import java.sql.Connection
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoField


object DataFrameHelper {

    val timeZoneConfig = ListTimeZone.getTimeZoneId()

    fun createDataFrameFromQuery(conn: Connection, sql: String, verbose: Boolean = false): DataFrame {
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)

        if (verbose)
            println("Query executed")

        val ret = DataFrame.fromResultSet(rs)

        if (verbose)
            println("Data is load to Dataframe")

        return ret
    }

    fun readInt(line: Int, columnsDatabase: ColumnDatabase, data: DataFrame): Int {
        val columName: String = columnsDatabase.nameDatabase
        val row: DataFrameRow = data.row(line)
        val ret: Any? = row.get(columName)
        if (ret is Long)
            return ret.toInt()
        if (ret is Int)
            return ret
        throw RuntimeException("Type Mismath")
    }

    fun readLong(line: Int, columnsDatabase: ColumnDatabase, data: DataFrame): Long {
        val columName: String = columnsDatabase.nameDatabase
        val row: DataFrameRow = data.row(line)
        val ret: Any? = row.get(columName)

        if (ret is Long)
            return ret

        if (ret is Int)
            return ret.toLong()

        if (ret is Double)
            return ret.toDouble().toLong()

        throw RuntimeException("Type Mismath in line ${line}, ${columnsDatabase.nameDatabase}")
    }

    fun readDouble(line: Int, columnsDatabase: ColumnDatabase, data: DataFrame): Double {
        val columName: String = columnsDatabase.nameDatabase
        val row: DataFrameRow = data.row(line)
        val ret: Any? = row.get(columName)

        if (ret is Double)
            return ret
        if (ret is Float)
            return ret.toDouble()
        throw RuntimeException("Type Mismath")
    }

    fun readFloat(line: Int, columnsDatabase: ColumnDatabase, data: DataFrame): Float {
        val columName: String = columnsDatabase.nameDatabase
        val row: DataFrameRow = data.row(line)
        val ret: Any? = row.get(columName)

        if (ret is Float)
            return ret
        if (ret is Double)
            return ret.toFloat()

        throw RuntimeException("Type Mismath")
    }

    fun readString(line: Int, columnsDatabase: ColumnDatabase, data: DataFrame): String {
        val columName: String = columnsDatabase.nameDatabase
        val row: DataFrameRow = data.row(line)
        return row.get(columName) as String
    }

    fun isNull(line: Int, columnsDatabase: ColumnDatabase, data: DataFrame): Boolean {
        val columName: String = columnsDatabase.nameDatabase
        val row: DataFrameRow = data.row(line)
        return (row.get(columName) == null)
    }

    @SuppressWarnings
    fun readDate(line: Int, columnsDatabase: ColumnDatabase, data: DataFrame): java.util.Date {
        val columName: String = columnsDatabase.nameDatabase
        val row: DataFrameRow = data.row(line)
        val value = row.get(columName)

        // Second precision.
        if (value is LocalDateTime)
            return Date(
                value.atZone(this.timeZoneConfig.toZoneId()).toEpochSecond() * 1000L
            )

        if (value is LocalDate)
            return Date(
                value.get(ChronoField.YEAR) - 1900,
                value.get(ChronoField.MONTH_OF_YEAR) - 1,
                value.get(ChronoField.DAY_OF_MONTH)
            )


        if (value is LocalTime) {
            val ret = LocalDateTime.of(LocalDate.now(), value)
            return Date(
                ret.atZone(this.timeZoneConfig.toZoneId()).toEpochSecond() * 1000L
            )
        }

        throw Exception("Column ${columnsDatabase.nameDatabase} is not defined as Date, check it.")
    }


    fun replaceAccentuation(value: String): String =
        value.replace('??', 'A')
            .replace('??', 'A')
            .replace('??', 'A')
            .replace('??', 'A')
            .replace('??', 'A')
            .replace('??', 'E')
            .replace('??', 'E')
            .replace('??', 'E')
            .replace('???', 'E')
            .replace('??', 'E')
            .replace('??', 'I')
            .replace('??', 'I')
            .replace('??', 'I')
            .replace('??', 'I')
            .replace('??', 'I')
            .replace('??', 'O')
            .replace('??', 'O')
            .replace('??', 'O')
            .replace('??', 'O')
            .replace('??', 'O')
            .replace('??', 'U')
            .replace('??', 'U')
            .replace('??', 'U')
            .replace('??', 'U')
            .replace('??', 'U')
            .replace('??', 'C')
            .replace('??', 'a')
            .replace('??', 'a')
            .replace('??', 'a')
            .replace('??', 'a')
            .replace('??', 'a')
            .replace('??', 'e')
            .replace('??', 'e')
            .replace('??', 'e')
            .replace('???', 'e')
            .replace('??', 'e')
            .replace('??', 'i')
            .replace('??', 'i')
            .replace('??', 'i')
            .replace('??', 'i')
            .replace('??', 'i')
            .replace('??', 'o')
            .replace('??', 'o')
            .replace('??', 'o')
            .replace('??', 'o')
            .replace('??', 'o')
            .replace('??', 'u')
            .replace('??', 'u')
            .replace('??', 'u')
            .replace('??', 'u')
            .replace('??', 'u')
            .replace('??', 'c')


    fun upperAndRemoveAccentuation(value: String): String =
        replaceAccentuation(value.uppercase())

    fun lowerAndRemoveAccentuation(value: String): String =
        replaceAccentuation(value.lowercase())

    private val onlyCharRegExp = Regex("[^A-Za-z ?????????????????????????????????????????????????????????????????????????????????????]")

    fun removeAccentuationAndRemoveNoLetters(value: String): String {
        return onlyCharRegExp.replace(
            replaceAccentuation(value), ""
        )
    }

    fun removeAccentuation(value: String): String = replaceAccentuation(value)


    fun manipuleStringsTypes(dataframe: DataFrame, columns: ColumnsDatabase): DataFrame {

        var retDataframe = dataframe

        var collunsManipulate = columns.filter { f ->
            f.hasChangeString()
        }


        collunsManipulate.forEach { c ->
            when (c.type) {
                // Convert String in UPPERCASE
                TypeColumn.STRING_UPPER -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it[c.nameDatabase + "__TO_MANIPULATE"].map<String>(String::uppercase)
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Convert String IN lowercase
                TypeColumn.STRING_LOWER -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it[c.nameDatabase + "__TO_MANIPULATE"].map<String>(String::lowercase)
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Convert String with without accentuation "??" -> "E", "??" -> "E" ...
                TypeColumn.STRING_UPPER_WITHOUT_ACCENTUATION -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it.get(c.nameDatabase + "__TO_MANIPULATE").map<String> { upperAndRemoveAccentuation(it) }
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Convert to lowercase and remove accentuation.
                TypeColumn.STRING_LOWER_WITHOUT_ACCENTUATION -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it.get(c.nameDatabase + "__TO_MANIPULATE").map<String> { lowerAndRemoveAccentuation(it) }
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Remove acentuation only
                TypeColumn.STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it.get(c.nameDatabase + "__TO_MANIPULATE")
                            .map<String> { removeAccentuationAndRemoveNoLetters(it) }
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Lowercase -> Change Accentuation -> Only Letters.
                TypeColumn.STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it.get(c.nameDatabase + "__TO_MANIPULATE")
                            .map<String> { onlyCharRegExp.replace(lowerAndRemoveAccentuation(it), "") }
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }
                // UPPERCASE -> Change accentuation -> Only Letters.
                TypeColumn.STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it.get(c.nameDatabase + "__TO_MANIPULATE")
                            .map<String> { onlyCharRegExp.replace(upperAndRemoveAccentuation(it), "") }
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Only Letters and Numbers in Strings...
                TypeColumn.STRING_ONLY_LETTERS -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it.get(c.nameDatabase + "__TO_MANIPULATE")
                            .map<String> { onlyCharRegExp.replace(it, "") }
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                TypeColumn.STRING_WITHOUT_ACCENTUATION -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        it.get(c.nameDatabase + "__TO_MANIPULATE")
                            .map<String> { removeAccentuation(it) }
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }
            }
        }

        return retDataframe;
    }

}