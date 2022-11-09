package com.epcard.controllers

import com.epcard.models.OriginType
import com.epcard.models.Product
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class MySQLConnector {
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
    private val bioColName:String = "bio"
    private val connection:Connection = connect()
    /*init{
        try{
            Class.forName("org.mariadb.jdbc")
        } catch (e:Exception){
            error("mySQL connector init failed! " + e.message)
        }
    }*/

    private fun connect():Connection{
        try{
            return DriverManager.getConnection("jdbc:mariadb://$dbName:3306/epcardtest?user=$user&password=$pw")
        } catch (e:SQLException){
            error("mySQL connection failed! " + e.message)
        }
    }

    fun getProduct(index:Int):Product?{
        val query = connection.createStatement()
        val queryText = "SELECT * FROM $tableName WHERE $idName=${index+1}"
        val result = query.executeQuery(queryText)
        result.next()
        for(i in 1 until index) result.next()
        return makeProduct(result)

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
        /*product.apply {
            if(checkSqlInject(name) || checkSqlInject(password) || checkSqlInject(companyName)) {
                error("Someone tried tried SQL injection! KEKW")
            }
        }*/
        val query:Statement = connection.createStatement()
        val queryText:String = "INSERT INTO $tableName (`$nameColName`,`$pwColName`,`$companyName`,`$catColName`,`$originColName`,`$pelletColName`,`$recycledColName`,`$renewableColName`," +
                "`$recyclableColName`,`$reusableColName`,`$insColName`,`$coColName`,`$chColName`,`$reWasteColName`,`$bioColName`) VALUES ('${product.name}','${product.password}','${product.companyName}','${product.category}'," +
                "'${product.origin}',${product.pellet},${product.recycled},${product.renewable},${product.recyclable},${product.reused}," +
                "${product.hasInsulation},${product.co2},${product.ch4},${product.reuseWaste},${product.bio})"
        val result = query.executeQuery(queryText)
        result.next()
        if(result.warnings != null) result.warnings.message?.let { error(it) }
    }

    fun getAllProducts():ArrayList<Product>{
        val products:ArrayList<Product> = ArrayList()
        val query = connection.createStatement()
        val queryText = "SELECT * FROM $tableName"
        val result = query.executeQuery(queryText)
        result.next()
        while (!result.isLast) {
            makeProduct(result)?.let { products.add(it) }
            result.next()
        }
        return products

    }

    private fun makeProduct(result:ResultSet):Product?{
        val name =  result.getString(nameColName)
        val pw = result.getString(pwColName)
        if(pw == "") return null
        val company = result.getString(companyName)
        val category = result.getString(catColName)
        val origin = result.getString(originColName)
        val pellet  = result.getBoolean(pelletColName)
        val recycled  =  result.getBoolean(recycledColName)
        val renewable  = result.getBoolean(renewableColName)
        val recyclable  =  result.getBoolean(recyclableColName)
        val reusable  = result.getBoolean(reusableColName)
        val insulation  =  result.getBoolean(insColName)
        val co2  =  result.getFloat(coColName)
        val ch4  =  result.getFloat(chColName)
        val reWaste  =  result.getInt(reWasteColName)
        val bio = result.getBoolean(bioColName)
        //query.close()
        return Product(name = name, password = pw, companyName = company,category = category, origin = origin, pellet = pellet, recycled = recycled, renewable = renewable, recyclable = recyclable, hasInsulation = insulation, co2 = co2,ch4 = ch4, reuseWaste = reWaste, reused = reusable,bio = bio)
    }
    private fun checkSqlInject(input:String):Boolean{
        return input.matches(Regex("\\d+\\s?(or|OR)\\s?(.=.)+|\"\\s*(or|OR)\\s*\"\"=\"|;+\\s*\\w+"))
    }
}