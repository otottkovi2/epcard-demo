package com.epcard.controllers

import com.epcard.models.PackagingType
import com.epcard.models.Product
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class MySQLConnector {
    val connection:Connection = connect()
    val dbName:String = "zoli adatbázisa"
    val user:String = "user"
    val pw:String = "pw"
    val tableName:String = "table"
    val idName:String = "id"
    val nameColName:String = "név"
    val priceColName:String = "termelői ár"
    val packColName:String = "csomagolás"
    val greenColName:String = "zöld"
    val energyColName:String = "energia"
    val anExColName:String = "állatkísérlet"
    init{
        try{
            Class.forName("com.mysql.jdbc.driver")
        } catch (e:Exception){
            error("mySQL connector init failed! " + e.message)
        }
    }

    private fun connect():Connection{
        try{
            return DriverManager.getConnection("jdbc:mysql/${dbName}/user=${user}=password=${pw}")
        } catch (e:SQLException){
            error("mySQL connection failed! " + e.message)
        }
    }

    fun GetProduct(index:Int):Product?{
        val query = connection.createStatement()
        val queryText = "SELECT * FROM ${tableName} WHERE ${idName}=${index}"
        val result = query.executeQuery(queryText)
        result.next()
        for(i in 0..index) result.next()
        val name = if(result.getString(nameColName) == null) result.getString(nameColName) else return null
        val price = if(result.getString(priceColName) == null) result.getFloat(priceColName) else return null
        val pack  = if(result.getString(packColName) == null)
            when(result.getString(packColName)){
                "csomagolásmentes" -> PackagingType.NONE
                "üveg" -> PackagingType.GLASS
                "papír" -> PackagingType.PAPER
                "műanyag" ->
            }
            else return null
        val product = Product()

    }
}