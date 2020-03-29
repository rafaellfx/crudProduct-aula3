package com.example.productcrud.models

import java.io.Serializable

class Product(val id: String = "", var nome: String, var valor: Double) : Serializable {

    fun fromMap(): HashMap<String, String> {

        val pro = hashMapOf(
            "id" to this.id,
            "nome" to this.nome,
            "valor" to this.valor.toString()
        )

        return pro
    }
}