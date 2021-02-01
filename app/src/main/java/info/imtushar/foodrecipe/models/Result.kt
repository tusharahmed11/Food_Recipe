package info.imtushar.foodrecipe.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Result(
    val aggregateLikes: Int,
    val cheap: Boolean,
    val dairyFree: Boolean,
    val extendedIngredients: @RawValue List<ExtendedIngredient>,
    val glutenFree: Boolean,
    val healthScore: Int,

    @SerializedName("id")
    val recipeId: Int,
    val image: String,
    val likes: Int,
    val occasions: List<String>,
    val readyInMinutes: Int,
    val sourceName: String,
    val sourceUrl: String?,
    val title: String,
    val summary: String,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
): Parcelable