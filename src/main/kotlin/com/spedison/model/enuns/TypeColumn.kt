package com.spedison.model.enuns

sealed class TypeColumn (val typeString: String) {

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