package com.example.productcrud.server

import android.util.Log
import com.example.productcrud.models.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class ProductService {

    companion object {
        val firebase = FirebaseFirestore.getInstance().collection("products")

        fun findAll(): Task<QuerySnapshot> {

           return  firebase.get()

//                .addOnSuccessListener { request -> request }
//                .addOnFailureListener { execption ->
//                    Log.e("Error", execption.toString())
//                }


        }

        fun save(product: Product ) {
            firebase
                .add(product.fromMap())
                .addOnSuccessListener { documentReference ->
                    Log.d("TESTE", "${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("TESTE", "Error ", e)
                }
        }

        fun update(product: Product ) {
            firebase.document(product.id)
                .update(product.fromMap() as Map<String, Any>)
                .addOnSuccessListener { documentReference ->
                    Log.d("TESTE", "${documentReference}")
                }
                .addOnFailureListener { e ->
                    Log.w("TESTE", "Error ", e)
                }
        }

        fun delete(product: Product ) {
            firebase.document(product.id)
                .delete()
                .addOnSuccessListener { documentReference ->
                    Log.d("TESTE", "Feito")
                }
                .addOnFailureListener { e ->
                    Log.w("TESTE", "Error ", e)
                }
        }



    }
}