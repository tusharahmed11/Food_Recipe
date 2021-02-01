package info.imtushar.foodrecipe.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import info.imtushar.foodrecipe.data.Repository
import info.imtushar.foodrecipe.data.database.entities.FavoritesEntity
import info.imtushar.foodrecipe.data.database.entities.FoodJokeEntity
import info.imtushar.foodrecipe.data.database.entities.RecipesEntity
import info.imtushar.foodrecipe.models.FoodJoke
import info.imtushar.foodrecipe.models.RecipeResponse
import info.imtushar.foodrecipe.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /**ROOM DATABASE */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }
    }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipe() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<RecipeResponse>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<RecipeResponse>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQueries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQueries)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe =recipesResponse.value!!.data
                if (foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }

            }catch (e: Exception){
              //  recipesResponse.value = NetworkResult.Error(e.toString())
                recipesResponse.value = NetworkResult.Error("Something went wrong")
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQueries: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.searchRecipes(searchQueries)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)

            }catch (e: Exception){
                //  recipesResponse.value = NetworkResult.Error(e.toString())
                searchedRecipesResponse.value = NetworkResult.Error("Something went wrong")
            }
        }else{
            searchedRecipesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if (foodJoke != null){
                    offlineCacheFoodJoke(foodJoke)
                }

            }catch (e: Exception){
                //  recipesResponse.value = NetworkResult.Error(e.toString())
                foodJokeResponse.value = NetworkResult.Error("Something went wrong")
            }
        }else{
            foodJokeResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: RecipeResponse) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke){
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 ->{
                return NetworkResult.Error("API key Limited")
            }
            response.isSuccessful ->{
                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)
            }
            else ->{
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleFoodRecipesResponse(response: Response<RecipeResponse>): NetworkResult<RecipeResponse>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 ->{
                return NetworkResult.Error("API key Limited")
            }
            response.body()!!.results.isNullOrEmpty() ->{
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful ->{
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else ->{
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork ?: return false
        } else {
            connectivityManager.activeNetworkInfo.run {
                return when{
                    connectivityManager.activeNetworkInfo?.isConnected!! -> true
                    else -> false
                }
            }
        }
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->true
            else -> false
        }
    }
}