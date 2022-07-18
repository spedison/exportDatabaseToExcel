package com.spedison.model.enuns

sealed class TypeColumn (val typeString: String) {

    object STRING_UPPER : TypeColumn("str_upper")
    object STRING_LOWER : TypeColumn("str_lower")
    object STRING_LOWER_WITHOUT_ACCENTUATION : TypeColumn("str_lower_without_accentuation")
    object STRING_UPPER_WITHOUT_ACCENTUATION : TypeColumn("str_upper_without_accentuation")
    object STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS : TypeColumn("str_lower_without_accentuation_only_letters")
    object STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS : TypeColumn("str_upper_without_accentuation_only_letters")
    object STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION : TypeColumn("str_without_accentuation_only_letters")
    object STRING_ONLY_LETTERS : TypeColumn("str_only_letters")
    object STRING : TypeColumn("str")
    object INT : TypeColumn("int")
    object LONG : TypeColumn("long")
    object FLOAT : TypeColumn("float")
    object DOUBLE : TypeColumn("double")
    object DAY : TypeColumn("day")
    object HOUR : TypeColumn("time")
    object TIMESTAMP: TypeColumn("datetime")


    override fun toString(): String {
        return this.typeString
    }

    companion object {

        fun convertFromString(str: String): TypeColumn =
            when (str.trim().lowercase()) {
                "string" -> STRING
                "str" -> STRING
                "str_upper" -> STRING_UPPER
                "str_lower" -> STRING_LOWER
                "str_upper_only_letters" -> STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS
                "str_lower_only_letters" -> STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS
                "str_only_letters" ->  STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION
                "str_upper_without_accentuation" ->STRING_UPPER_WITHOUT_ACCENTUATION
                "str_lower_without_accentuation" ->STRING_LOWER_WITHOUT_ACCENTUATION
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