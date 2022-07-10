package com.spedison.helper

import org.apache.commons.cli.*


object ArgParse {


    private fun makeOptions(): Options {

        val options = Options()

        options
            .addOption("cd", "configdir", true,"Directory with config files.")

        options
            .addOption("sql", "sqlfile", true, "File with Sql File(s) query. Do not use extension \".sql\". " +
                    "Example --sqlfile report1,report2,report3. The number of queries have the same number of excel files."
            )

        options
            .addOption("xls", "xlsfile",true,"Output file(s) excel format. Example --xlsfile output1.xlsx,output2.xlsx,output3.xlsx")

        options
            .addOption("stz", "showtimezone",false,"Show all avaliable timezone in enviroment")

        options
            .addOption("sc", "showcols",false,"Show Columns of SQL File")

        options
            .addOption("ccf", "createcolsfile",false,"Create a configuration column file")

        options
            .addOption("sqlp", "sqlparam",true,"add param to SQL file. " +
                    "Format -sqlp \"KEY=VALUE\". You can repeat this options many times." +
                    "In .sql use %{KEY} for replace value. Example: in param use -sqlp \"LIMIT=10\" and " +
                    "in query file have -> select c1, c2 from table limit %{LIMIT}")

        options
            .addOption("help",false,"Show help")

        options
            .addOption("v", "verbose",false,"Turn verbose mode on. Show details while processing")

        return options
    }

    fun printHelp()  {
        val helper  =  HelpFormatter()
        helper.printHelp(" ", makeOptions())
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