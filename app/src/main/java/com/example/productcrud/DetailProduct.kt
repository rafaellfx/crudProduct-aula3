package com.example.productcrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.productcrud.models.Product
import com.example.productcrud.server.ProductService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_product.*


class DetailProduct : AppCompatActivity() {

    private lateinit var produto:Product;

    companion object {

        const val RESULT_EDIT = 1
        const val RESULT_DELETE = 2
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        this.produto = intent.getSerializableExtra("produto") as Product

        var nome = findViewById<EditText>(R.id.edNomeDetail).apply {
            setText(produto.nome)
        }
        var preco = findViewById<EditText>(R.id.edPrecoDetail).apply {
            setText(produto.valor.toString())
        }

    }

    fun editarProduto(v: View?) {

        this.produto.nome = edNomeDetail.text.toString()
        this.produto.valor = edPrecoDetail.text.toString().toDouble()

        ProductService.update(this.produto)

        val data = Intent()
        data.putExtra("produto", this.produto)
        setResult(RESULT_EDIT, data)
        finish()
    }

    fun excluirProduto(v: View?) {

        ProductService.delete(this.produto)

        setResult(RESULT_DELETE)
        finish()
    }
}
