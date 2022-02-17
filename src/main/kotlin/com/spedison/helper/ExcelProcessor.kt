package com.spedison.helper

import com.spedison.model.ColumnsDatabase
import com.spedison.model.InstructionSheet
import com.spedison.model.enuns.TypeColumn
import krangl.DataFrame
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.OutputStream

class ExcelProcessor(val fileNameExcelOutput: String, val columnsDatabase: ColumnsDatabase, val data: DataFrame) {

    val wb: Workbook = XSSFWorkbook()
    val outputStremXlsx: OutputStream
    var sheetsCount: Int = 0

    val cellDayFormat : CellStyle = wb.createCellStyle()
    val cellHourFormat : CellStyle = wb.createCellStyle()
    val cellDateTimeFormat : CellStyle = wb.createCellStyle()

    init {
        this.outputStremXlsx = FileOutputStream(this.fileNameExcelOutput)
        ajustCellStyles()
    }

    private fun ajustCellStyles() {
        val createHelper = wb.getCreationHelper()
        this.cellDayFormat.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yyyy"))
        this.cellDateTimeFormat.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yyyy h:mm:s"))
        this.cellHourFormat.setDataFormat(createHelper.createDataFormat().getFormat("h:mm:s"))
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
            page.setColumnWidth(col, column.lengthExcel)
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

            var colNum = 0
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
                    TypeColumn.DAY -> CellType.NUMERIC
                    TypeColumn.HOUR -> CellType.NUMERIC
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
    }

}