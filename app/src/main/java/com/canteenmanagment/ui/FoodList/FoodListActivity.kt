package com.canteenmanagment.ui.FoodList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.Fragments.MenuFragment.Companion.CATEGORY_NAME
import com.canteenmanagment.Fragments.ProfileFragment.Companion.FAVOURITE
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.ActivityFoodListBinding
import com.canteenmanagment.ui.CartFoodList.CartFoodList
import com.canteenmanagment.utils.AddCartCustomDialog
import kotlinx.coroutines.launch

class FoodListActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityFoodListBinding
    private val mContext: Context = this
    private lateinit var foodList: List<Food>
    private lateinit var addCartCustomDialog: AddCartCustomDialog
    private var flag = false
    private lateinit var favFoodList: MutableList<Food>

    private var isFavouriteActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_list)

        isFavouriteActivity = intent.getBooleanExtra(FAVOURITE, false)
        binding.title = if (!isFavouriteActivity) intent.getStringExtra(CATEGORY_NAME) else "Favourite Food"
        binding.IMback.setOnClickListener(this)

        loadData()

        addCartCustomDialog = AddCartCustomDialog(this, { food -> addToFav(food) }, { food -> removeFromFav(food) })

        binding.SRRefreshLayout.setOnRefreshListener { loadData() }

        binding.BTOrderList.setOnClickListener {
            val intent = Intent(mContext, CartFoodList::class.java)
            startActivity(intent)
        }

        scope.launch {
            favFoodList = FirebaseApiManager.getAllFavouriteFoods().let {
                if (it.isSuccess) it.data as MutableList<Food> else mutableListOf()
            }
        }

        // Apply custom edge effect to RecyclerView
        binding.RVFoodList.edgeEffectFactory = BounceEdgeEffectFactory(mContext)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.IMback) onBackPressed()
    }

    private fun loadData() {
        if (!isFavouriteActivity) {
            scope.launch {
                FirebaseApiManager.getAllFoodFromCategory(intent.getStringExtra(CATEGORY_NAME)!!).let {
                    binding.SRRefreshLayout.isRefreshing = false
                    foodList = it
                    binding.RVFoodList.visibility = View.VISIBLE
                    binding.RVFoodList.adapter = FoodListRecyclerViewAdapter(it, FoodListRecyclerViewAdapter.ClickListner { position ->
                        var flag = favFoodList.contains(foodList[position])
                        if (foodList[position].available) {
                            addCartCustomDialog.startDialog(
                                foodList[position],
                                true,
                                { getDataFromSharedPreferences() },
                                isFavorite = flag,
                                isFavVisible = true
                            )
                        }
                    })
                }
            }
        } else {
            scope.launch {
                FirebaseApiManager.getAllFavouriteFoods().let {
                    if (it.isSuccess) {
                        binding.SRRefreshLayout.isRefreshing = false
                        foodList = it.data as List<Food>
                        binding.RVFoodList.visibility = View.VISIBLE
                        binding.RVFoodList.adapter = FoodListRecyclerViewAdapter(foodList, FoodListRecyclerViewAdapter.ClickListner { position ->
                            var flag = favFoodList.contains(foodList[position])
                            if (foodList[position].available) {
                                addCartCustomDialog.startDialog(
                                    foodList[position],
                                    true,
                                    { getDataFromSharedPreferences() },
                                    isFavorite = flag,
                                    isFavVisible = true
                                )
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        binding.CL.visibility = View.INVISIBLE
        super.onBackPressed()
    }

    private fun addToFav(food: Food) {
        scope.launch {
            FirebaseApiManager.addFoodToFavourite(food).let {
                if (it.isSuccess) favFoodList.add(food)
            }
        }
    }

    private fun removeFromFav(food: Food) {
        scope.launch {
            FirebaseApiManager.removeFoodFromFavourite(food).let {
                if (it.isSuccess) favFoodList.remove(food)
            }
        }
    }

    private fun getDataFromSharedPreferences() {
        val preference = application.getSharedPreferences(FoodListActivity.CART, 0x0000)
        val cartItemString = preference.getString(FoodListActivity.CART_ITEMS, null)
        flag = cartItemString != null && cartItemString != "[]"
        binding.BTOrderList.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val CART = "Cart"
        const val CART_ITEMS = "Cart Items"
    }
}
