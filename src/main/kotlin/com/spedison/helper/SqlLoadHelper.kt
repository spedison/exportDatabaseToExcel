package com.spedison.helper

import java.io.File
import java.nio.charset.Charset

object SqlLoadHelper {

    fun loadSqlFromFile (fileName : String, verbose:Boolean = true):String {
        val ret = File(fileName)
            .inputStream()
            .readBytes()
            .toString(Charset.defaultCharset())

        if (verbose)
            println(ret)

        return ret
    }
}