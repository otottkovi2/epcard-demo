package com.epcard.controllers

import com.epcard.models.OriginType
import com.epcard.models.Product
import com.epcard.models.ProductCategory
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class MySQLConnector {
    private val connection:Connection = connect()
    private val dbName:String = "zoli adatbázisa"
    private val user:String = "user"
    private val pw:String = "pw"
    private val tableName:String = "table"
    private val idName:String = "id"
    private val nameColName:String = "név"
    private val catColName:String = "kategória"
    private val originColName:String = "származási hely"
    private val pelletColName:String = "raklap"
    private val recycledColName:String = "újrahasznosított"
    private val renewableColName:String = "megújuló energia"
    private val recyclableColName:String = "újrahasznosítható"
    private val reusableColName:String = "újrafelhasználható"
    private val insColName:String = "szigetelés"
    private val coColName:String = "co2 kibocsátás"
    private val chColName:String = "ch4 kibocsátás"
    private val reWasteColName:String = "újrahasznosított szemét"
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

    fun getProduct(index:Int):Product?{
        val query = connection.createStatement()
        val queryText = "SELECT * FROM $tableName WHERE $idName=${index+1}"
        val result = query.executeQuery(queryText)
        query.close()
        result.next()
        for(i in 0..index) result.next()
        val name =  result.getString(nameColName)
        if(name == "null") return null
            val catEnum = when(result.getInt(catColName)){
                0 -> ProductCategory.ELECTRONICS
                1 -> ProductCategory.BEAUTY
                2 -> ProductCategory.CAR
                3 -> ProductCategory.CAR_ACCESSORY
                4 -> ProductCategory.FASHION
                5 -> ProductCategory.HEALTH
                6 -> ProductCategory.HOME
                7 -> ProductCategory.OFFICE
                8 -> ProductCategory.SPORT
                9 -> ProductCategory.TOYS
                10 -> ProductCategory.FOOD
                else -> throw NoSuchElementException()
            }
            val orgEnum = when(result.getInt(originColName)){
                0 -> OriginType.DOMESTIC
                1 -> OriginType.IMPORTEDDOMESTIC
                2 -> OriginType.IMPORTED
                else -> throw NoSuchElementException()
            }
        val pellet  = result.getBoolean(pelletColName)
        val recycled  =  result.getBoolean(recycledColName)
        val renewable  = result.getBoolean(renewableColName)
        val recyclable  =  result.getBoolean(recyclableColName)
        val reusable  = result.getBoolean(reusableColName)
        val insulation  =  result.getBoolean(insColName)
        val co2  =  result.getFloat(coColName)
        val ch4  =  result.getFloat(chColName)
        val reWaste  =  result.getInt(reWasteColName)
        return Product(name = name, category = catEnum, origin = orgEnum, pellet = pellet, recycled = recycled, renewable = renewable, recyclable = recyclable, hasInsulation = insulation, co2 = co2,ch4 = ch4, reuseWaste = reWaste, reused = reusable)

    }

    fun length():Int{
        val query:Statement = connection.createStatement()
        val queryText = "SELECT count(*) FROM $tableName"
        val result = query.executeQuery(queryText)
        result.next()
        val len = result.getInt(0)
        query.close()

        return len
    }
    fun addItem(product: Product){
        val query:Statement = connection.createStatement()
        val queryText:String = "INSERT INTO $tableName ('$nameColName','$catColName','$originColName','$pelletColName','$recycledColName','$renewableColName'," +
                "'$recyclableColName','$reusableColName','$insColName','$coColName','$chColName','$reWasteColName') VALUES ('${product.name}',${product.category.ordinal}," +
                "${product.origin.ordinal},${product.pellet},${product.recycled},${product.renewable},${product.recyclable},${product.reused}," +
                "${product.hasInsulation},${product.co2},${product.ch4},${product.reuseWaste})"
        val result = query.executeQuery(queryText)
        if(result.warnings != null) result.warnings.message?.let { error(it) }
    }
}