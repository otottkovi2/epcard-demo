package com.epcard.controllers

import com.epcard.models.Product
import io.ktor.server.plugins.*

class DbController {
    private val db:MySQLConnector = MySQLConnector()
    lateinit var products:ArrayList<Product>

    init {
        fillList()
    }

    fun fillList(){
        products = ArrayList()
        var i = 0
        while (i < db.length()){
            db.getProduct(i)?.let { products.add(it)  }
            i++
        }
    }

    fun addProduct(product: Product){
        db.addItem(product)
        fillList()
    }

    fun getProduct(index: Int): Product {
        return db.getProduct(index) ?: throw NotFoundException()
    }

}