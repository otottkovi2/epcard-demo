package com.epcard.models

import com.epcard.controllers.ControllerFactory.Factory
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val name: String, val password:String, val companyName:String, val category: String, val origin:String, val pellet:Boolean, val recycled:Boolean, val reused:Boolean,
    val renewable:Boolean, val recyclable:Boolean, val hasInsulation:Boolean, val co2: Int, val ch4: Int, val reuseWaste:Int,
    val bio:Boolean, val weight:Int) {
    val score = calcScore()
    val rating:ProductRating = when(score) {
        in 48..60 -> ProductRating.LIGHTGREEN
        in 36..48 -> ProductRating.GREEN
        in 24..36 -> ProductRating.BLUE
        in 12..24 -> ProductRating.YELLOW
        else -> ProductRating.RED
    }
    val reuseWeight:Float = if (weight != 0)(reuseWaste/ weight).toFloat() else 0f

    fun updateRatingScore(){
        try {

            Factory.controller.updateRating(name, rating)
            Factory.controller.updateScore(name, score)
        } catch (e: Exception) {
            e.message?.let { error(it) }
        }
    }
    private fun calcScore():Int{
        var sum = 0
        if(bio) sum += 5
        if(recyclable) sum += 5
        when(ch4){
            in 100 downTo  0 -> sum += 5
        }
        when(co2){
            in 100 downTo  0 -> sum += 5
        }
        if(hasInsulation) sum += 5
        if(pellet) sum += 5
        if(origin == "Hungary") sum += 5
        if(recycled) sum += 5
        if(recyclable) sum += 5
        if(reused) sum += 5
        if(renewable) sum += 5
        when(reuseWaste){
            43 -> sum += 5
        }
        return sum
    }

}
enum class ProductCategory{
    ELECTRONICS,HOME,OFFICE,BEAUTY,HEALTH,TOYS,CAR,CAR_ACCESSORY,SPORT,FASHION,FOOD
}
enum class OriginType{
    DOMESTIC,IMPORTEDDOMESTIC,IMPORTED
}

enum class ProductRating{
    LIGHTGREEN,GREEN,BLUE,YELLOW,RED
}



