package info.imtushar.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import info.imtushar.foodrecipe.databinding.RecipeRowLayoutBinding
import info.imtushar.foodrecipe.models.RecipeResponse
import info.imtushar.foodrecipe.models.Result
import info.imtushar.foodrecipe.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {


    private var recpies = emptyList<Result>()
    class MyViewHolder(private val binding: RecipeRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result){
            binding.recipe = result
        }

        companion object{
            fun from(parent: ViewGroup) : MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recpies[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recpies.size
    }
    fun setData(newData: RecipeResponse){
        val recipesDiffUtil = RecipesDiffUtil(recpies,newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recpies = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}