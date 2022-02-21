package com.spedison.model

import com.spedison.model.enuns.TypeColumn

data class ColumnDatabase(
    val nameExcel: String,
    val nameDatabase: String,
    val type: TypeColumn,
    val lengthColumnDatabase: Int,
    val lengthExcel: Int) {

    override fun toString(): String {
        return "${this.nameExcel},${this.nameDatabase},${this.type},${this.lengthExcel},${this.lengthColumnDatabase}"
    }
}
