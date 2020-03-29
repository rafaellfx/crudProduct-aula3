package com.example.productcrud

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.productcrud.models.Product
import com.example.productcrud.server.ProductService
import com.google.firebase.firestore.FirebaseFirestore


class CadastroProduto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_produto)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun cadastrarProduto(view: View?) {

        val textName = findViewById(R.id.edName) as EditText
        val textValor = findViewById(R.id.edValor) as EditText

        val produto = Product("",textName.text.toString(),  textValor.text.toString().toDouble())

        ProductService.save(produto)

        val it = Intent().apply {
            putExtra("produto", produto)
        }
        setResult(Activity.RESULT_OK, it)

        finish()
    }

    fun cancelarCadastro(view: View?) {
        finish()
    }
}
