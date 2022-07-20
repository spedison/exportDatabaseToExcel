package com.spedison.database


/**
 * Command Line Format is -sqlp "KEY=VALUE"
 *
 */
data class VariableSQL(val nome: String, val valor: String) {

    companion object {

        // From String method receive is "KEY=VALUE" Static Methos
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
