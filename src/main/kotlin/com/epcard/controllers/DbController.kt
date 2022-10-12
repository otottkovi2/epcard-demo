package com.epcard.controllers

import com.epcard.models.Product

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
            products.add(db.getProduct(i) as Product)
            i++
        }
    }

    fun addProduct(product: Product){
        db.addItem(product)
        fillList()
    }



}