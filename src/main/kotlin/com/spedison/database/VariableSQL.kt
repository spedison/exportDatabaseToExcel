package com.spedison.database


/**
 * Formato da linha de comando -p "CHAVE=VALOR"
 */
data class VariableSQL(val nome: String, val valor: String) {
    companion object {
        fun fromString(str: String): VariableSQL {
            val strs = str.split("=")
            if (strs.size != 2) {
                println("Problemas ao criar a Variável SQL. Tamanho diferente de 2")
                return VariableSQL("null","")
            }
            return VariableSQL(strs.get(0),strs.get(1))
        }
    }
}
