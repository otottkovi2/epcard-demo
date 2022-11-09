package com.epcard.controllers

import com.epcard.models.Product
import com.epcard.models.ProductRating
import io.ktor.server.plugins.*

class DbController {
    companion object Factory{
        lateinit var instance:DbController
    }
    private val db: MySQLConnector = MySQLConnector()
    lateinit var products: ArrayList<Product>

    init {
        fillList()
    }

    fun fillList() {
        products = db.getAllProducts()
    }

    fun addProduct(product: Product) {
        try {
            db.addItem(product)
        } catch (e: Exception) {
            println("submitting product failed! " + e.message)
        }
        fillList()
    }

    fun getProduct(index: Int): Product {
        return db.getProduct(index) ?: throw NotFoundException()
    }

    fun updateRating(index: Int,rating: ProductRating){
        db.updateProduct(index,"rateing",rating.ordinal)
    }

    fun updateScore(index: Int,score:Int){
        db.updateProduct(index,"score",score)
    }

}