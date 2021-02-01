package info.imtushar.foodrecipe.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import info.imtushar.foodrecipe.models.RecipeResponse
import info.imtushar.foodrecipe.models.Result

class RecipesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: RecipeResponse) : String{
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToRecipeResponse(data: String): RecipeResponse{
        val listType = object : TypeToken<RecipeResponse>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String{
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result{
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }
}