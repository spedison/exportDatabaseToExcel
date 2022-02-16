import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.OutputStream

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: \n${args.joinToString(separator = "\n")}")


    // Criando um Workbook xlsx
    val wb : Workbook = XSSFWorkbook()
    val outputStremXlsx : OutputStream = FileOutputStream( "saida.xlsx")

    val pagina : Sheet = wb.createSheet()
    pagina.autoSizeColumn(1)
    pagina.setColumnWidth(0,10000) // = 100 -> 0,05 Cm, 10000 -> 7,68 Cm

    val linha : Row = pagina.createRow(0)
    linha.height = 850 // 350 = 0,61 cm

    val celula1 = linha.createCell(0)
    celula1.setCellValue("Essa célula é uma String")

    val celula2 = linha.createCell(1)

    val cellStyle2 = wb.createCellStyle()
    cellStyle2.fillBackgroundColor = IndexedColors.BLUE.index
    cellStyle2.fillPattern = FillPatternType.BIG_SPOTS
    val fonte : Font = wb.createFont()
    fonte.setFontHeight(600) // 300 - Equivale a 15,
    fonte.fontName = "Courier New"
    fonte.italic = true
    fonte.bold = true
    cellStyle2.setFont(fonte)
    cellStyle2.alignment =HorizontalAlignment.CENTER
    celula2.cellStyle = cellStyle2



    val cellStyle1 = wb.createCellStyle()
    val fonte1 : Font = wb.createFont()
    fonte1.setFontHeight(300) // 300 - Equivale a 15,
    fonte1.fontName = "Source Code Pro"
    fonte1.italic = true
//    fonte.bold = true
    cellStyle1.setFont(fonte1)
    cellStyle1.alignment =HorizontalAlignment.CENTER
    cellStyle1.verticalAlignment =VerticalAlignment.CENTER
    celula1.cellStyle = cellStyle1


    // println(cellStyle)

    celula2.setCellValue(10.0)

    wb.write(outputStremXlsx)

    outputStremXlsx.close()

}