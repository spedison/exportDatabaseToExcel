import com.spedison.database.ConectionDatabase
import com.spedison.database.VariableSQL
import com.spedison.helper.*
import com.spedison.model.ColumnsDatabase
import com.spedison.model.InstructionSheet
import org.apache.commons.cli.CommandLine
import java.io.File
import java.nio.file.Paths.*
import java.sql.Connection
import java.util.LinkedList

class MainJDBC {

    private var showColsInfo: Boolean = false
    private var baseConfigDir: String = ""
    private var conectionDatabase: ConectionDatabase = ConectionDatabase()
    private val columns: LinkedList<ColumnsDatabase> = LinkedList<ColumnsDatabase>()
    private var sqlFile: LinkedList<String> = LinkedList<String>()
    private var xlsFile: LinkedList<String> = LinkedList<String>()
    private var query: LinkedList<String> = LinkedList<String>()
    private var variablesSQL: LinkedList<VariableSQL> = LinkedList<VariableSQL>()
    private var verbose: Boolean = false


    fun main(args: Array<String>) {

        // Verify arguments and process it.
        val argsParsed = ArgParse.parse(args)

        if (argsParsed.hasOption("help")) {
            ArgParse.printHelp()
            return
        }

        if (argsParsed.hasOption("showtimezone")) {
            ListTimeZone.print()
            return
        }

        // Set São Paulo/Brasil as default value.
        setDefaultTimeZone()

        // Load arguments and defauls for processing.
        loadAllParameters(argsParsed)

        if (xlsFile.size != query.size) {
            println("List of Queries and list of files must be same size.")
            ArgParse.printHelp()
        }


        // Set On/Off verbose
        conectionDatabase.setVerbose(verbose)

        // Locad Conecction Properties
        conectionDatabase.load("${baseConfigDir}connection.properties")

        // Open conection database
        val conn: Connection = conectionDatabase.getConection()

        // Show Columns or create configuration File.
        if (showColsInfo) {
            for (i in 0..sqlFile.size - 1) {
                if (argsParsed.hasOption("createcolsfile")) {
                    columns.get(i).getColumns(query.get(i), conn)
                    columns.get(i).writeConfiguration(File("${sqlFile.get(i)}-columns.csv"))
                    println("Configuartion file \"${sqlFile.get(i)}-columns.csv\" has created.")
                } else {
                    // Show Columns of query
                    ShowCols.print(query.get(i), conn, false)
                }
            }
        } else {
            for (i in 0..sqlFile.size - 1) {
                createExcelFile(conn, query.get(i), xlsFile.get(i), sqlFile.get(i), columns.get(i))
            }
        }

        // close database
        conn.close()
    }

    private fun loadAllParameters(argsParsed: CommandLine) {

        verbose = argsParsed.hasOption("verbose")

        showColsInfo = (argsParsed.hasOption("showcols")
                || argsParsed.hasOption("createcolsfile"))

        // Start :: Get All Arguments or Default Values
        baseConfigDir = argsParsed.getOptionValue("configdir") ?: System.getProperty("user.dir")
        if (!baseConfigDir.endsWith(File.separator)) {
            baseConfigDir += File.separatorChar
        }

        argsParsed.options.filter { it.longOpt.equals("sqlparam") }
            .map { VariableSQL.fromString(it.value) }
            .forEach(variablesSQL::add)

        // Make Sql Base file locacation: <sqlname>, <sqlname>.sql, <sqlname>-columns.csv, <sqlname>-instructions.properties
        val localListSqlFile = argsParsed.getOptionValue("sqlfile", "default").split(",")
        localListSqlFile.forEach {
            val localFile = get(baseConfigDir, "queries", "${it}.sql").toString()
            sqlFile.add(localFile)
            query.add(SqlLoadHelper.loadSqlFromFile(localFile, verbose))

            // Load coluns configurations, if will not be show fields data
            if (showColsInfo == false) {
                val columnsToadd = ColumnsDatabase()
                columnsToadd.readFileConfiguration(File(localFile.replace(".sql", "-columns.csv")))
                columns.add(columnsToadd)
            }
        }

        val listListXlsFile = (argsParsed.getOptionValue("xlsfile") ?: "output.xlsx").split(",")
        listListXlsFile.forEach {
            xlsFile.add(it)
        }
    }

    private fun createExcelFile(
        conn: Connection,
        query: String,
        xlsFile: String,
        sqlFile: String,
        columns: ColumnsDatabase,
    ) {

        var queryChanged = query
        variablesSQL.forEach {
            queryChanged = queryChanged.replace("%{${it.nome}}", it.valor)
        }

        if (verbose)
            println("This query executed : ${queryChanged} ")

        // Dataframe to Work :-)
        val data = DataFrameHelper.createDataFrameFromQuery(conn, queryChanged, verbose)

        // Create Excel Processor (Sorted or not)
        val excelProcessor = if (!columns.fieldsSortedAsc.isEmpty()) {
            ExcelProcessor(
                xlsFile, columns,
                data.sortedBy(* columns.fieldsSortedAsc.split(",").toTypedArray()),
                verbose
            )
        } else if (!columns.fieldsSortedDesc.isEmpty()) {
            ExcelProcessor(
                xlsFile, columns,
                data.sortedByDescending(* columns.fieldsSortedDesc.split(",").toTypedArray()),
                verbose
            )
        } else {
            ExcelProcessor(xlsFile, columns, data, verbose)
        }

        // If filename instructions exists then create Instructions Sheet in Excel file.
        val instructions = InstructionSheet(verbose)
        val filenameInstructions = sqlFile.replace(".sql", "-instructions.properties")
        if (instructions.loadFromFile(filenameInstructions)) {
            excelProcessor.createInstructionSheet(instructions)
        }

        // Copy data from dataframe to excel
        excelProcessor.processData()

        // Save to File
        excelProcessor.save()
    }

    private fun setDefaultTimeZone() {
        if (System.getProperty("user.timezone") == null ||
            System.getProperty("user.timezone").trim().isEmpty()
        )
            System.setProperty("user.timezone", "America/Sao_Paulo")
    }
}

fun main(args: Array<String>) {
    val main = MainJDBC()
    main.main(args)
}
