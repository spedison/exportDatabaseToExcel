package com.spedison.model.enuns

import org.apache.commons.compress.utils.Lists
import java.util.LinkedList
import java.util.stream.Collectors
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

sealed class TypeColumn(val typeString: String, val helpMessage: String) {

    object STRING_UPPER : TypeColumn("str_upper", "String field, after read data convert data to Uppercase.")
    object STRING_LOWER : TypeColumn("str_lower", "String field, after read data convert data to Lowercase.")
    object STRING_LOWER_WITHOUT_ACCENTUATION : TypeColumn(
        "str_lower_without_accentuation",
        "String field, after read data convert data to lowercase, remove accentuation (é->e, à->a...)."
    )

    object STRING_UPPER_WITHOUT_ACCENTUATION : TypeColumn(
        "str_upper_without_accentuation",
        "String field, after read data convert data to uppercase, remove accentuation (É->e, Á->a...)."
    )

    object STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS : TypeColumn(
        "str_lower_without_accentuation_only_letters",
        "String field, after read data convert data to lowercase, remove accentuation and remove no letters (-01ÉeÁx->eeax)."
    )

    object STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS : TypeColumn(
        "str_upper_without_accentuation_only_letters",
        "String field, after read data convert data to uppercase, remove accentuation and remove no letters (-01ÉeÁx->EEAX)."
    )

    object STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION : TypeColumn(
        "str_only_letters_without_accentuation",
        "String field, after read data convert remove accentuation and remove no letters(\"-01Ée.Áx\"->EeAx)."
    )

    object STRING_ONLY_LETTERS :
        TypeColumn("str_only_letters", "String field, after read data convert remove no letters(\"-01Ée.Áx\"->ÉaÁx).")

    object STRING_WITHOUT_ACCENTUATION : TypeColumn(
        "str_without_accentuation",
        "String field, after read data remove accentuation(\"-01Ée.Áx\"->\"-01Ea.Ax\")."
    )

    object STRING : TypeColumn("str", "Field String with no transformations")
    object INT : TypeColumn("int", "Integer field")
    object LONG : TypeColumn("long", "Long number field")
    object FLOAT : TypeColumn("float", "Float decimal field")
    object DOUBLE : TypeColumn("double", "Double decimal field")
    object DAY : TypeColumn("day", "Day information field. dd/mm/yyyy")
    object HOUR : TypeColumn("time", "Time information field. HH:MM:SS")
    object TIMESTAMP : TypeColumn("datetime", "Timestamp field Data and Time field. ")


    fun toHelpString(): String =
        "Field: %-40s - %s".format(typeString, helpMessage)

    override fun toString(): String {
        return this.typeString
    }

    companion object {

        private val allObjects = listOf(
            STRING_UPPER,
            STRING_LOWER,
            STRING_LOWER_WITHOUT_ACCENTUATION,
            STRING_UPPER_WITHOUT_ACCENTUATION,
            STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS,
            STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS,
            STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION,
            STRING_ONLY_LETTERS,
            STRING_WITHOUT_ACCENTUATION,
            STRING,
            INT,
            LONG,
            FLOAT,
            DOUBLE,
            DAY,
            HOUR,
            TIMESTAMP
        )


        fun showHelpFields(): String {
            val ret: StringBuffer = StringBuffer()
            allObjects.map(TypeColumn::toHelpString).map { it -> it + "\n" }.forEach(ret::append)
            return ret.toString()
        }

        fun convertFromString(str: String): TypeColumn =
            when (str.trim().lowercase()) {
                "string" -> STRING
                "str" -> STRING
                "str_upper" -> STRING_UPPER
                "str_lower" -> STRING_LOWER
                "str_only_letters_without_accentuation" -> STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION
                "str_only_letters" -> STRING_ONLY_LETTERS
                "str_without_accentuation" -> STRING_WITHOUT_ACCENTUATION
                "str_upper_without_accentuation_only_letters" -> STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS
                "str_lower_without_accentuation_only_letters" -> STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS
                "str_upper_without_accentuation" -> STRING_UPPER_WITHOUT_ACCENTUATION
                "str_lower_without_accentuation" -> STRING_LOWER_WITHOUT_ACCENTUATION
                "int" -> INT
                "integer" -> INT
                "long" -> LONG
                "frac" -> FLOAT
                "float" -> FLOAT
                "double" -> DOUBLE
                "day" -> DAY
                "date" -> DAY
                "hour" -> HOUR
                "time" -> HOUR
                "timestamp" -> TIMESTAMP
                "datetime" -> TIMESTAMP
                else -> throw Exception("Problems while capture type in coluns configuration file. Please,Check type : ${str}")
            }
    }
}