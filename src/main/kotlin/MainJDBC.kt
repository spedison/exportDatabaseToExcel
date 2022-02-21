import com.spedison.database.ConectionDatabase
import com.spedison.helper.*
import com.spedison.model.ColumnsDatabase
import com.spedison.model.InstructionSheet
import org.apache.commons.cli.CommandLine
import java.io.File
import java.sql.Connection

class MainJDBC {

    private var showColsInfo: Boolean = false
    private var baseConfigDir: String = ""
    private var conectionDatabase: ConectionDatabase = ConectionDatabase()
    private val colunms = ColumnsDatabase()
    private var sqlFile: String = ""
    private var xlsFile: String = ""
    private var query : String = ""
    private var verbose : Boolean = false

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

        // Load coluns configurations, if will not be show fields data
        if (showColsInfo == false)
            colunms.readFileConfiguration(File("${sqlFile}-columns.csv"))

        // Set On/Off verbose
        conectionDatabase.setVerbose(verbose)

        // Locad Conecction Properties
        conectionDatabase.load("${baseConfigDir}connection.properties")

        // Open conection database
        val conn: Connection = conectionDatabase.getConection()

        // Show Columns or create configuration File.
        if (showColsInfo) {

            if (argsParsed.hasOption("createcolsfile")) {
                colunms.getColumns(query, conn)
                colunms.writeConfiguration(File("${sqlFile}-columns.csv"))
                println("Configuartion file \"${sqlFile}-columns.csv\" has created.")
            } else {
                // Show Columns of query
                ShowCols.print(query, conn, false)
            }
        } else {

            createExcelFile(conn, query, xlsFile, sqlFile)
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

        // Make Sql Base file locacation: <sqlname>, <sqlname>.sql, <sqlname>-columns.csv, <sqlname>-instructions.properties
        sqlFile = (baseConfigDir + "queries" + File.separator +
                ((argsParsed.getOptionValue("sqlfile") ?: "default").lowercase()).removeSuffix(".sql"))

        xlsFile = argsParsed.getOptionValue("xlsfile") ?: "output.xlsx"

        // Load SQL Query
        query = SqlLoadHelper.loadSqlFromFile("${sqlFile}.sql")
    }

    private fun createExcelFile(
        conn: Connection,
        query: String,
        xlsFile: String,
        sqlFile: String,
    ) {
        // Dataframe to Work :-)
        val data = DataFrameHelper.createDataFrameFromQuery(conn, query, verbose)

        // Create Excel Processor
        val excelProcessor = ExcelProcessor(xlsFile, colunms, data, verbose)

        // If filename instructions exists then create Instructions Sheet in Excel file.
        val instructions = InstructionSheet(verbose)
        val filenameInstructions = "${sqlFile}-instructions.properties"
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
