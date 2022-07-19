package com.spedison.helper

import com.spedison.model.ColumnDatabase
import com.spedison.model.ColumnsDatabase
import com.spedison.model.enuns.TypeColumn
import krangl.DataFrame
import krangl.DataFrameRow
import krangl.fromResultSet
import krangl.rename
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

        throw RuntimeException("Type Mismath")
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
        value.replace('Á', 'A')
            .replace('À', 'A')
            .replace('Â', 'A')
            .replace('Ã', 'A')
            .replace('Ä', 'A')
            .replace('É', 'E')
            .replace('È', 'E')
            .replace('Ê', 'E')
            .replace('Ẽ', 'E')
            .replace('Ë', 'E')
            .replace('Í', 'I')
            .replace('Ì', 'I')
            .replace('Î', 'I')
            .replace('Ĩ', 'I')
            .replace('Ï', 'I')
            .replace('Ó', 'O')
            .replace('Ò', 'O')
            .replace('Ô', 'O')
            .replace('Ô', 'O')
            .replace('Ö', 'O')
            .replace('Ú', 'U')
            .replace('Ù', 'U')
            .replace('Û', 'U')
            .replace('Ũ', 'U')
            .replace('Ü', 'U')
            .replace('Ç', 'C')
            .replace('á', 'a')
            .replace('à', 'a')
            .replace('â', 'a')
            .replace('ã', 'a')
            .replace('ä', 'a')
            .replace('é', 'e')
            .replace('è', 'e')
            .replace('ê', 'e')
            .replace('ẽ', 'e')
            .replace('ë', 'e')
            .replace('í', 'i')
            .replace('ì', 'i')
            .replace('î', 'i')
            .replace('ĩ', 'i')
            .replace('ï', 'i')
            .replace('ó', 'o')
            .replace('ò', 'o')
            .replace('ô', 'o')
            .replace('ô', 'o')
            .replace('ö', 'o')
            .replace('ú', 'u')
            .replace('ù', 'u')
            .replace('û', 'u')
            .replace('ũ', 'u')
            .replace('ü', 'u')
            .replace('ç', 'c')


    fun upperAndRemoveAccentuation(value: String): String =
        replaceAccentuation(value.uppercase())

    fun lowerAndRemoveAccentuation(value: String): String =
        replaceAccentuation(value.lowercase())

    private val onlyCharRegExp = Regex("[^A-Za-z 0-9áÁéÉíÍóÓúÚàÀèÈìÌòÒùÙâÂêÊîÎôÔûÛãÃẽÊĩĨõÕũŨçÇ]")

    fun removeAccentuationAndRemoveNoLetters(value: String): String {
        return onlyCharRegExp.replace(
            replaceAccentuation(value), ""
        )
    }

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
                        (it[c.nameDatabase + "__TO_MANIPULATE"] + "  11")
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Convert String IN lowercase
                TypeColumn.STRING_LOWER -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        (it.get(c.nameDatabase + "__TO_MANIPULATE") as String).lowercase()
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Convert String with without accentuation "é" -> "e", "Ê" -> "E" ...
                TypeColumn.STRING_UPPER_WITHOUT_ACCENTUATION -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        upperAndRemoveAccentuation((it.get(c.nameDatabase + "__TO_MANIPULATE") as String))
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Convert to lowercase and remove accentuation.
                TypeColumn.STRING_LOWER_WITHOUT_ACCENTUATION -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        lowerAndRemoveAccentuation((it.get(c.nameDatabase + "__TO_MANIPULATE") as String))
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Remove acentuation only
                TypeColumn.STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        removeAccentuationAndRemoveNoLetters((it.get(c.nameDatabase + "__TO_MANIPULATE") as String))
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Lowercase -> Change Accentuation -> Only Letters.
                TypeColumn.STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        onlyCharRegExp.replace(
                            lowerAndRemoveAccentuation(it.get(c.nameDatabase + "__TO_MANIPULATE") as String),
                            ""
                        )
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }
                // UPPERCASE -> Change accentuation -> Only Letters.
                TypeColumn.STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        onlyCharRegExp.replace(
                            upperAndRemoveAccentuation(it.get(c.nameDatabase + "__TO_MANIPULATE") as String),
                            ""
                        )
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }

                // Only Letters and Numbers in Strings...
                TypeColumn.STRING_ONLY_LETTERS -> {
                    retDataframe = retDataframe.rename(c.nameDatabase to c.nameDatabase + "__TO_MANIPULATE")
                    retDataframe = retDataframe.addColumn(c.nameDatabase) {
                        onlyCharRegExp.replace((it.get(c.nameDatabase + "__TO_MANIPULATE") as String), "")
                    }
                    retDataframe = retDataframe.remove(c.nameDatabase + "__TO_MANIPULATE")
                }
            }
        }

        return retDataframe;
    }

}