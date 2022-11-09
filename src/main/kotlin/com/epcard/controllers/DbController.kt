package com.epcard.controllers

import com.epcard.models.Product
import io.ktor.server.plugins.*

class DbController {
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

}