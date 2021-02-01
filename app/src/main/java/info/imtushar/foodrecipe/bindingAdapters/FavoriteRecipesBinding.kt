package info.imtushar.foodrecipe.bindingAdapters

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import info.imtushar.foodrecipe.adapters.FavoriteRecipesAdapter
import info.imtushar.foodrecipe.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {
    companion object{

        @BindingAdapter("viewVisibility","setData",requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoritesEntity>?,
            adapter: FavoriteRecipesAdapter?
        ){
            if (favoritesEntity.isNullOrEmpty()){
                when(view){
                    is LinearLayout ->{
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            }else{
                when(view){
                    is LinearLayout ->{
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        adapter?.setData(favoritesEntity)
                    }
                }
            }
        }
    }
}