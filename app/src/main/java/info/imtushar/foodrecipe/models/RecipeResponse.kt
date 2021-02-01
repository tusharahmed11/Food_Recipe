package info.imtushar.foodrecipe.models


data class RecipeResponse(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)