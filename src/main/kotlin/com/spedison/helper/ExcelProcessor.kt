package com.spedison.helper

import com.spedison.model.ColumnsDatabase
import com.spedison.model.InstructionSheet
import com.spedison.model.enuns.TypeColumn
import krangl.DataFrame
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.OutputStream

class ExcelProcessor {

    val wb: Workbook = XSSFWorkbook()
    val fileNameExcelOutput: String
    val outputStremXlsx: OutputStream
    val columnsDatabase: ColumnsDatabase
    val data: DataFrame
    var sheetsCount: Int = 0

    constructor(fileNameExcelOutput: String, columnsDatabase: ColumnsDatabase, data: DataFrame) {
        this.fileNameExcelOutput = fileNameExcelOutput
        this.outputStremXlsx = FileOutputStream(this.fileNameExcelOutput)
        this.columnsDatabase = columnsDatabase
        this.data = data
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

    private fun createHeaderAndWidth(page: Sheet): Unit {

        val line: Row = page.createRow(0)

        line.height = (line.height + 100).toShort()
        val style = wb.createCellStyle()
        val font = wb.createFont()
        font.fontHeight = (font.fontHeight + 30).toShort()
        font.bold = true
        style.setFont(font)
        var col: Int = 0
        for (column in columnsDatabase) {
            page.setColumnWidth(col, column.lengthExcel)
            val cell = line.createCell(col)
            cell.setCellValue(column.nameExcel)
            cell.cellStyle = style
            col++
        }
    }

    fun processData(): Unit {

        // Create new Page in Excel File
        val page: Sheet = wb.createSheet("Data")
        sheetsCount++

        // Create Header and Ajust Width Size
        createHeaderAndWidth(page)

        // Iterate in lines of dataframe
        for (line: Int in (1..data.nrow)) {

            var colNum: Int = 0
            val row: Row = page.createRow(line)
            // Iterate in Coluns of Dataframe
            for (col in columnsDatabase) {

                // Define CellType
                val cellType: CellType = when (col.type) {
                    TypeColumn.STRING -> CellType.STRING
                    TypeColumn.INT -> CellType.NUMERIC
                    TypeColumn.LONG -> CellType.NUMERIC
                    TypeColumn.DOUBLE -> CellType.NUMERIC
                    TypeColumn.FLOAT -> CellType.NUMERIC
                    TypeColumn.TIMESTAMP -> CellType.NUMERIC
                    else -> CellType.STRING
                }

                // Create one Cel
                val cel = row.createCell(colNum, cellType)

                // Set Value of Cell
                val lineData = line - 1
                if (DataFrameHelper.isNull(lineData, col, data)) {
                    cel.setBlank()
                } else {
                    when (col.type) {
                        TypeColumn.STRING -> cel.setCellValue(DataFrameHelper.readString(lineData, col, data))
                        TypeColumn.INT -> cel.setCellValue(DataFrameHelper.readInt(lineData, col, data).toDouble())
                        TypeColumn.LONG -> cel.setCellValue(DataFrameHelper.readLong(lineData, col, data).toDouble())
                        TypeColumn.DOUBLE -> cel.setCellValue(DataFrameHelper.readDouble(lineData, col, data))
                        TypeColumn.FLOAT -> cel.setCellValue(DataFrameHelper.readFloat(lineData, col, data).toDouble())
                        TypeColumn.TIMESTAMP -> cel.setCellValue(DataFrameHelper.readDate(lineData, col, data))
                        TypeColumn.DAY -> cel.setCellValue(DataFrameHelper.readDate(lineData, col, data))
                        TypeColumn.HOUR -> cel.setCellValue(DataFrameHelper.readDate(lineData, col, data))
                    }
                }
                // Go to Next Column cel.
                colNum++
            }
        } // End Iterate of Dataframe
    } // End Method

    fun save(): Unit {


        //TODO : Set password to Edit File.
        wb.write(outputStremXlsx)
        outputStremXlsx.close()
    }

}