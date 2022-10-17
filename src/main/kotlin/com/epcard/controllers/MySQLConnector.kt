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
    private val dbName:String = "84.21.182.8"
    private val user:String = "tomi"
    private val pw:String = "Maszat19"
    private val tableName:String = "industrys"
    private val idName:String = "id"
    private val nameColName:String = "name"
    private val pwColName:String = "password"
    private val companyName:String = "company"
    private val catColName:String = "category"
    private val originColName:String = "place_of_origin"
    private val pelletColName:String = "pallet"
    private val recycledColName:String = "recycled"
    private val renewableColName:String = "renewable_energy"
    private val recyclableColName:String = "can_be_recycled"
    private val reusableColName:String = "reusable"
    private val insColName:String = "insulation"
    private val coColName:String = "co2"
    private val chColName:String = "ch4"
    private val reWasteColName:String = "recycled_garbage"
    /*init{
        try{
            Class.forName("org.mariadb.jdbc")
        } catch (e:Exception){
            error("mySQL connector init failed! " + e.message)
        }
    }*/

    private fun connect():Connection{
        println(dbName)
        try{
            return DriverManager.getConnection("jdbc:mariadb://84.21.182.8:3306/epcardtest?user=tomi&password=Maszat19")
        } catch (e:SQLException){
            error("mySQL connection failed! " + e.message)
        }
    }

    fun getProduct(index:Int):Product?{
        val query = connection.createStatement()
        val queryText = "SELECT * FROM $tableName WHERE $idName=${index+1}"
        val result = query.executeQuery(queryText)
        result.next()
        for(i in 0 until index) result.next()
        val name =  result.getString(nameColName)
        val pw = result.getString(pwColName)
        if(pw == "") return null
        val company = result.getString(companyName)
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
        //query.close()
        return Product(name = name, password = pw, companyName = company,category = catEnum, origin = orgEnum, pellet = pellet, recycled = recycled, renewable = renewable, recyclable = recyclable, hasInsulation = insulation, co2 = co2,ch4 = ch4, reuseWaste = reWaste, reused = reusable)

    }

    fun length():Int{
        val query:Statement = connection.createStatement()
        val queryText = "SELECT count(*) FROM $tableName"
        val result = query.executeQuery(queryText)
        result.next()
        val len = result.getInt(1)
        query.close()

        return len
    }
    fun addItem(product: Product){
        val query:Statement = connection.createStatement()
        val queryText:String = "INSERT INTO $tableName (`$nameColName`,`$pwColName`,`$companyName`,`$catColName`,`$originColName`,`$pelletColName`,`$recycledColName`,`$renewableColName`," +
                "`$recyclableColName`,`$reusableColName`,`$insColName`,`$coColName`,`$chColName`,`$reWasteColName`) VALUES ('${product.name}','${product.password}','${product.companyName}',${product.category.ordinal}," +
                "${product.origin.ordinal},${product.pellet},${product.recycled},${product.renewable},${product.recyclable},${product.reused}," +
                "${product.hasInsulation},${product.co2},${product.ch4},${product.reuseWaste})"
        val result = query.executeQuery(queryText)
        if(result.warnings != null) result.warnings.message?.let { error(it) }
    }
}