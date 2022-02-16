package com.spedison.helper

import org.apache.commons.cli.*


object ArgParse {



    private fun makeOptions(): Options {

        val options = Options()

        options
            .addOption("cd", "configdir", true,"Directory with condif files.");

        options
            .addOption("sql", "sqlfile", true, "File with Sql File query. Do not use extension \".sql\". Example --sqlfile report1");

        options
            .addOption("xls", "xlsfile",true,"Output file excel format. Example --xlsfile output.xlsx");

        options
            .addOption("create", "xlsfile",true,"Output file excel format. Example --xlsfile output.xlsx");

        options
            .addOption("help",false,"Show help")

        return options
    }

    fun printHelp() : Unit {
        val helper : HelpFormatter =  HelpFormatter()
        helper.printHelp(" ", makeOptions());
    }

    fun parse(args: Array<String>): CommandLine {
        val parser: CommandLineParser = DefaultParser()
        val options = this.makeOptions()
        try {
            return parser.parse(options, args)
        } catch (e: ParseException) {
            printHelp()
            throw e
        }
    }
}