package info.imtushar.foodrecipe.data

import info.imtushar.foodrecipe.data.network.FoodRecipesApi
import info.imtushar.foodrecipe.models.FoodJoke
import info.imtushar.foodrecipe.models.RecipeResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
){

    suspend fun getRecipes(queries: Map<String, String>): Response<RecipeResponse>{
        return  foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(queries: Map<String, String>): Response<RecipeResponse>{
        return  foodRecipesApi.searchRecipes(queries)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke>{
        return foodRecipesApi.getFoodJoke(apiKey)
    }
}