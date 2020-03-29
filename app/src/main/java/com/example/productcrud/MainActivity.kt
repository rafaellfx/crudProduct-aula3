package com.example.productcrud

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productcrud.adapter.ProductAdapter
import com.example.productcrud.models.Product
import com.example.productcrud.server.ProductService
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(),ProductAdapter.OnItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar: Toolbar

    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE  = 2;
    private var listaProduct: ArrayList<Product> = ArrayList()
    private var posicaoAlterar=-1

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ProductAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if(currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter = ProductAdapter(listaProduct)
        viewAdapter.onItemClickListener = this

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu,menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.signOut -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if(currentUser != null){
            _init()
        }else{
           startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    override fun onItemClicked(view: View, position: Int) {
        val it = Intent(this, DetailProduct::class.java)
        this.posicaoAlterar = position
        val produtos = listaProduct.get(position)
        it.putExtra("produto", produtos)
        startActivityForResult(it, REQ_DETALHE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CADASTRO) {

            if (resultCode == Activity.RESULT_OK) {

                val produto = data?.getSerializableExtra("produto") as Product
                listaProduct.add(produto)
                viewAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Cadastro realizada com sucesso!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == REQ_DETALHE) {
            if (resultCode == DetailProduct.RESULT_EDIT) {
                val produto = data?.getSerializableExtra("produto") as Product
                listaProduct.set(this.posicaoAlterar, produto)
                viewAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Edicao realizada com sucesso!", Toast.LENGTH_SHORT)
                    .show()
            } else if (resultCode == DetailProduct.RESULT_DELETE) {
                listaProduct.removeAt(this.posicaoAlterar)
                viewAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Exclusao realizada com sucesso!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun _init() {
        listaProduct.clear()
        ProductService.findAll().addOnSuccessListener { p ->
            p.map { p -> listaProduct.add(Product(p.id, p.data["nome"].toString(), p.data["valor"].toString().toDouble()))
                viewAdapter.notifyDataSetChanged()
            }
        }
    }

    fun abrirFormulario(view: View) {
        val it = Intent(this, CadastroProduto::class.java)
        startActivityForResult(it, REQ_CADASTRO)
    }



}
