package info.imtushar.foodrecipe.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import info.imtushar.foodrecipe.models.RecipeResponse
import info.imtushar.foodrecipe.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: RecipeResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}