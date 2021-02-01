package info.imtushar.foodrecipe.models


import com.google.gson.annotations.SerializedName

data class Ingredient(
    val id: Int,
    val image: String,
    val localizedName: String,
    val name: String
)