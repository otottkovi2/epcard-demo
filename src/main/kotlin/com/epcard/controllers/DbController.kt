package com.epcard.controllers

import com.epcard.models.Product
import com.epcard.models.ProductRating
import io.ktor.server.plugins.*
import java.sql.SQLException

class DbController {

    private val db: MySQLConnector = MySQLConnector()
    val products: ArrayList<Product> = ArrayList()


    init {
        try {
            db.connect()
            //fillList()
        } catch (e: SQLException) {
            e.message?.let { error(it) }
        }

    }

    /*fun fillList() {
        for(item in db.getAllProducts()){
            if(!products.contains(item)){
                products.add(item)
            }
        }
    }*/

    fun addProduct(product: Product) {
        try {
            db.addItem(product)
        } catch (e: Exception) {
            println("submitting product failed! " + e.message)
        }
        //fillList()
    }

    fun getProduct(index: Int): Product? {
        return db.getProduct(index)
    }

    fun getProduct(name: String): Product? {
        return db.getProduct(name)
    }

    fun updateRating(name: String, rating: ProductRating) {
        try {
            db.updateProduct(name, "rateing", rating.ordinal)
        } catch (e: Exception) {
            e.message?.let { error(it) }
        }

    }

    fun updateScore(name: String, score: Int) {
        try {
            db.updateProduct(name, "score", score)
        } catch (e: Exception) {
            e.message?.let { error(it) }
        }

    }

}