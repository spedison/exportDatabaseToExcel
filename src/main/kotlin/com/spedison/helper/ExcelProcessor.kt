package com.spedison.helper

import com.spedison.model.ColumnsDatabase
import com.spedison.model.InstructionSheet
import com.spedison.model.enuns.TypeColumn
import krangl.DataFrame
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.min

class ExcelProcessor(private val fileNameExcelOutput: String, private val columnsDatabase: ColumnsDatabase, private val data: DataFrame, private val verbose: Boolean = false) {

    private val wb: Workbook = XSSFWorkbook()
    private val outputStremXlsx: OutputStream
    private var sheetsCount: Int = 0

    private val cellDayFormat : CellStyle = wb.createCellStyle()
    private val cellHourFormat : CellStyle = wb.createCellStyle()
    private val cellDateTimeFormat : CellStyle = wb.createCellStyle()

    init {
        this.outputStremXlsx = FileOutputStream(this.fileNameExcelOutput)
        ajustCellStyles()
    }

    private fun ajustCellStyles() {
        val createHelper = wb.creationHelper
        this.cellDayFormat.dataFormat = createHelper.createDataFormat().getFormat("d/m/yyyy")
        this.cellDateTimeFormat.dataFormat = createHelper.createDataFormat().getFormat("d/m/yyyy h:mm:s")
        this.cellHourFormat.dataFormat = createHelper.createDataFormat().getFormat("h:mm:s")

        if (verbose)
            println("Style is created.")
    }

    fun createInstructionSheet(instructionSheet: InstructionSheet) {
        val page: Sheet = wb.createSheet(instructionSheet.sheetName)
        sheetsCount++
        val line: Row = page.createRow(instructionSheet.row)
        val cell = line.createCell(instructionSheet.col)
        line.height = instructionSheet.height.toShort()
        page.setColumnWidth(instructionSheet.col, instructionSheet.width)

        val cellStyleInstruction = wb.createCellStyle()
        val font = wb.createFont()

        font.fontHeight = (200 + font.fontHeight).toShort()
        cellStyleInstruction.setFont(font)
        cellStyleInstruction.alignment = HorizontalAlignment.CENTER
        cellStyleInstruction.verticalAlignment = VerticalAlignment.CENTER
        cellStyleInstruction.wrapText = true

        cell.cellStyle = cellStyleInstruction
        cell.setCellValue(instructionSheet.text)
    }

    private fun createHeaderAndWidth(page: Sheet) {

        val line: Row = page.createRow(0)

        line.height = (line.height + 100).toShort()
        val style = wb.createCellStyle()
        val font = wb.createFont()
        font.fontHeight = (font.fontHeight + 30).toShort()
        font.bold = true
        style.setFont(font)
        var col = 0

        for (column in columnsDatabase) {
            page.setColumnWidth(col,  min( column.lengthExcel, 250*250) )
            val cell = line.createCell(col)
            cell.setCellValue(column.nameExcel)
            cell.cellStyle = style
            col++
        }
    }

    fun processData() {

        // Create new Page in Excel File
        val page: Sheet = wb.createSheet("Data")
        sheetsCount++

        // Create Header and Ajust Width Size
        createHeaderAndWidth(page)

        // Iterate in lines of dataframe
        for (line: Int in (1..data.nrow)) {

            // Row num to process.
            var colNum = 0

            // Create Excel Row.
            val row: Row = page.createRow(line)

            // Set Value of Cell
            val lineData = line - 1

            if (verbose)
                println("Line Dataframe ${line} = " + data.row(lineData))

            // Iterate in Coluns of Dataframe
            for (col in columnsDatabase) {

                // Define Excel CellType
                val cellType: CellType = when (col.type) {
                    TypeColumn.STRING -> CellType.STRING
                    TypeColumn.STRING_LOWER_WITHOUT_ACCENTUATION -> CellType.STRING
                    TypeColumn.STRING_UPPER_WITHOUT_ACCENTUATION -> CellType.STRING
                    TypeColumn.STRING_LOWER -> CellType.STRING
                    TypeColumn.STRING_UPPER -> CellType.STRING
                    //TODO: Add All fields.
                    TypeColumn.INT -> CellType.NUMERIC
                    TypeColumn.LONG -> CellType.NUMERIC
                    TypeColumn.DOUBLE -> CellType.NUMERIC
                    TypeColumn.FLOAT -> CellType.NUMERIC
                    TypeColumn.TIMESTAMP -> CellType.NUMERIC
                    TypeColumn.DAY -> CellType.NUMERIC
                    TypeColumn.HOUR -> CellType.NUMERIC
                }

                // Create one Cel
                val cel = row.createCell(colNum, cellType)


                if (DataFrameHelper.isNull(lineData, col, data)) {
                    cel.setBlank()
                } else {
                    when (col.type) {
                        TypeColumn.STRING -> cel.setCellValue(DataFrameHelper.readString(lineData, col, data))
                        TypeColumn.INT -> cel.setCellValue(DataFrameHelper.readInt(lineData, col, data).toDouble())
                        TypeColumn.LONG -> cel.setCellValue(DataFrameHelper.readLong(lineData, col, data).toDouble())
                        TypeColumn.DOUBLE -> cel.setCellValue(DataFrameHelper.readDouble(lineData, col, data))
                        TypeColumn.FLOAT -> cel.setCellValue(DataFrameHelper.readFloat(lineData, col, data).toDouble())
                        TypeColumn.TIMESTAMP -> {
                            cel.setCellValue(DataFrameHelper.readDate(lineData, col, data))
                            cel.cellStyle = this.cellDateTimeFormat
                        }
                        TypeColumn.DAY -> {
                            cel.setCellValue(DataFrameHelper.readDate(lineData, col, data))
                            cel.cellStyle = this.cellDayFormat
                        }
                        TypeColumn.HOUR -> {
                            cel.setCellValue(DataFrameHelper.readDate(lineData, col, data))
                            cel.cellStyle = this.cellHourFormat
                        }
                    }
                }
                // Go to Next Column cel.
                colNum++
            }
        } // End Iterate of Dataframe
    } // End Method

    fun save() {
        //TODO : Set password to Edit File.
        wb.write(outputStremXlsx)
        outputStremXlsx.close()
        if (verbose)
            println("Excel File  ${fileNameExcelOutput} was writed.")
    }

}