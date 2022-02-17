import com.spedison.database.ConectionDatabase
import com.spedison.helper.*
import com.spedison.model.ColumnsDatabase
import com.spedison.model.InstructionSheet
import java.io.File
import java.sql.Connection


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

    // Start :: Get All Arguments or Default Values
    var baseConfigDir: String =
        argsParsed.getOptionValue("configdir") ?: System.getProperty("user.dir")

    if (!baseConfigDir.endsWith(File.separator)) {
        baseConfigDir += File.separatorChar
    }

    val sqlFile: String = (baseConfigDir + "queries" + File.separator +
            ((argsParsed.getOptionValue("sqlfile") ?: "default").lowercase()).removeSuffix(".sql"))

    val xlsFile: String = argsParsed.getOptionValue("xlsfile") ?: "output.xlsx"
    // End :: Get All Arguments or Default Values

    // Locad Conecction Properties
    val conectionDatabase = ConectionDatabase("${baseConfigDir}connection.properties")
    conectionDatabase.load()

    // Load coluns configurations
    val colunms = ColumnsDatabase()
    colunms.readFileConfiguration(File("${sqlFile}-columns.csv"))

    // Load SQL Query
    val query = SqlLoadHelper.loadSqlFromFile("${sqlFile}.sql")

    // Open conection database
    val conn: Connection = conectionDatabase.getConection()

    // Dataframe to Work :-)
    val data = DataFrameHelper.createDataFrameFromQuery(conn, query)

    // Create Excel Processor
    val excelProcessor = ExcelProcessor(xlsFile, colunms, data)

    // If filename instructions exists then create Instructions Sheet in Excel file.
    val instructions = InstructionSheet()
    val filenameInstructions = "${sqlFile}-instructions.properties"
    if (instructions.loadFromFile(filenameInstructions)) {
        excelProcessor.createInstructionSheet(instructions)
    }

    // Copy data from dataframe to excel
    excelProcessor.processData()

    // close database
    conn.close()

    // Save to File
    excelProcessor.save()

}
