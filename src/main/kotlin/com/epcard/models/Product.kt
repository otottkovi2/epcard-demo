package com.epcard.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(val name: String,val password:String,val companyName:String,val category:ProductCategory,val origin:OriginType,val pellet:Boolean,val recycled:Boolean,val reused:Boolean,
                   val renewable:Boolean,val recyclable:Boolean, val hasInsulation:Boolean,val co2:Float,val ch4:Float,val reuseWaste:Int) {
    var discount:Float = calcRating()

    private fun calcRating():Float{
        var sum = -(origin.ordinal)+co2+ch4+reuseWaste
        if(pellet) sum++
        if(recycled) sum++
        if(reused) sum++
        if(renewable) sum++
        if(recyclable) sum++
        if(hasInsulation) sum++
        return when(sum){
            in Float.MIN_VALUE..10f -> 0.2f
            in 10f..15f -> 0.1f
            in 15f..25f -> 0.05f
            else -> 0f
        }
    }

}
enum class ProductCategory{
    ELECTRONICS,HOME,OFFICE,BEAUTY,HEALTH,TOYS,CAR,CAR_ACCESSORY,SPORT,FASHION,FOOD
}
enum class OriginType{
    DOMESTIC,IMPORTEDDOMESTIC,IMPORTED
}



