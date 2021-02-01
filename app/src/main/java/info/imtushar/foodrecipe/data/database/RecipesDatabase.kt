package info.imtushar.foodrecipe.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import info.imtushar.foodrecipe.data.database.entities.FavoritesEntity
import info.imtushar.foodrecipe.data.database.entities.FoodJokeEntity
import info.imtushar.foodrecipe.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoritesEntity::class, FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase(){
    abstract fun recipesDao(): RecipeDao
}