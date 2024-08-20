//package com.example.moviesapp.ui.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.navigation.findNavController
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.moviesapp.R
//import com.example.moviesapp.data.models.Category
//import com.example.moviesapp.databinding.CategoryItemBinding
//import com.example.moviesapp.ui.CategoryFragmentDirections
//
//
//class CategoryAdapter(
//    private val categoryList: MutableList<Category>,
//    private val context: Context
//) :
//    RecyclerView.Adapter<CategoryViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
//        val binding =
//            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CategoryViewHolder(binding)
//    }
//
//    override fun getItemCount() = categoryList.size
//
//    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        holder.categoryName.text = categoryList[position].name
//        //holder.rating.rating = categoryList[position].rating
//        Glide
//            .with(context)
//            .load(
//                categoryList[position].image
//            )
//            .placeholder(R.drawable.image_placeholder)
//            .into(holder.image)
//
//
//        holder.itemView.setOnClickListener {
//
//
//            val action =
//                CategoryFragmentDirections.actionCategoryFragmentToMoviesFragment(
//                    categoryList[position].id,
//                    categoryList[position].name
//                )
//            it.findNavController().navigate(action)
//        }
//    }
//}
//
//class CategoryViewHolder(categoryItemView: CategoryItemBinding) :
//    RecyclerView.ViewHolder(categoryItemView.root) {
//    val image = categoryItemView.categoryImage
//    val categoryName = categoryItemView.categoryName
//    //val rating = categoryItemView.ratingBar
//}
package com.example.moviesapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.models.Category
import com.example.moviesapp.databinding.CategoryItemBinding
import com.example.moviesapp.databinding.MovieCategoryDetailBinding
import com.example.moviesapp.ui.CategoryFragmentDirections


class CategoryAdapter(
    private val categoryList: MutableList<Category>,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    var viewType: Int = VIEW_TYPE_ONE

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ONE -> {
                val view =
                    CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CategoryViewHolder(view)
            }

            VIEW_TYPE_TWO -> {
                val view = MovieCategoryDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                MovieCategoryViewHolder(view)
            }

            else -> {
                throw IllegalArgumentException("Invalid view type")

            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (holder.itemViewType) {

            VIEW_TYPE_TWO -> {
                (holder as MovieCategoryViewHolder)
                holder.categoryName.text = categoryList[position].name
            }

            VIEW_TYPE_ONE -> {
                (holder as CategoryViewHolder)
                holder.categoryName.text = categoryList[position].name
                //holder.rating.rating = categoryList[position].rating
                Glide
                    .with(context)
                    .load(
                        categoryList[position].image
                    )
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.image)


                holder.itemView.setOnClickListener {


                    val action =
                        CategoryFragmentDirections.actionCategoryFragmentToMoviesFragment(
                            categoryList[position].id,
                            categoryList[position].name
                        )
                    it.findNavController().navigate(action)
                }
            }


        }
    }

    override fun getItemCount() = categoryList.size
}

class MovieCategoryViewHolder(categoryItemView: MovieCategoryDetailBinding) :
    RecyclerView.ViewHolder(categoryItemView.root) {
    //    val image = categoryItemView.categoryImage
    val categoryName = categoryItemView.categoryName
    //val rating = categoryItemView.ratingBar
}

class CategoryViewHolder(categoryItemView: CategoryItemBinding) :
    RecyclerView.ViewHolder(categoryItemView.root) {
    val image = categoryItemView.categoryImage
    val categoryName = categoryItemView.categoryName
    //val rating = categoryItemView.ratingBar
}


//    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        holder.categoryName.text = categoryList[position].name
//        //holder.rating.rating = categoryList[position].rating
//        Glide
//            .with(context)
//            .load(
//                categoryList[position].image
//            )
//            .placeholder(R.drawable.image_placeholder)
//            .into(holder.image)
//
//
//        holder.itemView.setOnClickListener {
//
//
//            val action =
//                CategoryFragmentDirections.actionCategoryFragmentToMoviesFragment(
//                    categoryList[position].id,
//                    categoryList[position].name
//                )
//            it.findNavController().navigate(action)
//        }
//    }


