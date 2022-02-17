package com.spedison.model.enuns

sealed class TypeColumn {

    object STRING : TypeColumn()
    object INT : TypeColumn()
    object LONG : TypeColumn()
    object FLOAT : TypeColumn()
    object DOUBLE : TypeColumn()
    object DAY : TypeColumn()
    object HOUR : TypeColumn()
    object TIMESTAMP: TypeColumn()

    companion object {
        fun convertFromString(str: String): TypeColumn =
            when (str.trim().lowercase()) {
                "string" -> STRING
                "str" -> STRING
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