package info.imtushar.foodrecipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import info.imtushar.foodrecipe.models.Result
import info.imtushar.foodrecipe.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)