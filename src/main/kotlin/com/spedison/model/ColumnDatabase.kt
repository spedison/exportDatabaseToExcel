package com.spedison.model

import com.spedison.model.enuns.TypeColumn

data class ColumnDatabase(
    val nameExcel: String,
    val nameDatabase: String,
    val type: TypeColumn,
    val lengthColumnDatabase: Int,
    val lengthExcel: Int,
    val addToExcel: Boolean //TODO: Use this field to Write Excel file.
) {

    override fun toString(): String {
        return "${this.nameExcel},${this.nameDatabase},${this.type},${this.lengthExcel},${this.lengthColumnDatabase},${this.addToExcel}"
    }

    fun hasChangeString(): Boolean =
        this.type.equals(TypeColumn.STRING_UPPER) or
                this.type.equals(TypeColumn.STRING_LOWER) or
                this.type.equals(TypeColumn.STRING_LOWER_WITHOUT_ACCENTUATION) or
                this.type.equals(TypeColumn.STRING_UPPER_WITHOUT_ACCENTUATION) or
                this.type.equals(TypeColumn.STRING_UPPER_WITHOUT_ACCENTUATION_ONLY_LETTERS) or
                this.type.equals(TypeColumn.STRING_LOWER_WITHOUT_ACCENTUATION_ONLY_LETTERS) or
                this.type.equals(TypeColumn.STRING_ONLY_LETTERS_WITHOUT_ACCENTUATION)

}
