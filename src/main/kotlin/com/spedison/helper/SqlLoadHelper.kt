package com.spedison.helper

import java.io.File

object SqlLoadHelper {

    fun loadSqlFromFile (fileName : String):String =
         File(fileName)
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
}