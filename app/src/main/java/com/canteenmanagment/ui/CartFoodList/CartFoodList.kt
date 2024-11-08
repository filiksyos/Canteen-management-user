package com.canteenmanagment.ui.CartFoodList

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnTouchListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.Fragment.Home.FavoriteFoodRecyclerViewAdapter
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.ActivityCartFoodListBinding
import com.canteenmanagment.helper.showShortToast
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.canteenmanagment.utils.AddCartCustomDialog
import com.canteenmanagment.utils.CustomProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class CartFoodList : BaseActivity() {

    private lateinit var cartFoodListViewModel: CartFoodListViewModel
    private lateinit var binding: ActivityCartFoodListBinding
    private lateinit var addCartCustomDialog: AddCartCustomDialog
    private var cartFoodList: MutableList<CartFood> = mutableListOf()
    private var mContext: Context = this
    private val customProgressBar: CustomProgressBar = CustomProgressBar(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_food_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_food_list)
        cartFoodListViewModel = ViewModelProviders.of(this).get(CartFoodListViewModel::class.java)

        binding.RVCartFood.setOnTouchListener(OnTouchListener { _, _ -> true })

        addCartCustomDialog = AddCartCustomDialog(this)
        getDataFromSharedPreferences()

        cartFoodListViewModel.cartFoodList.observe(this, Observer {
            cartFoodList = it // Update the local cartFoodList variable

            binding.RVCartFood.adapter = CartFoodListRecyclerViewAdapter(it) { position ->
                addCartCustomDialog.startDialog(cartFoodList[position].food, true)
                true // Return true as a Boolean directly
            }

            val arrangedFood = arrangeFood(cartFoodList)
            binding.textView10.visibility = if (arrangedFood.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            binding.RVRecommendedFood.adapter = FavoriteFoodRecyclerViewAdapter(arrangedFood) { position ->
                addCartCustomDialog.startDialog(arrangedFood[position], true)
                true // Return true as a Boolean directly
            }
        })

        binding.IMBack.setOnClickListener {
            super.onBackPressed()
        }

        // Directly placing the order without UPI payment
        binding.BTPlaceOrder.setOnClickListener {
            placeOrder("469204901")
        }
    }

    private fun placeOrder(transactionId: String) {
        customProgressBar.startDialog()
        scope.launch {
            FirebaseApiManager.placeOrderInSystem(cartFoodList, transactionId).let {
                customProgressBar.stopDiaolog()
                if (it.isSuccess) {
                    val editor = mContext.getSharedPreferences(FoodListActivity.CART, 0x0000).edit()
                    editor.remove(FoodListActivity.CART_ITEMS)
                    editor.apply()
                    finish()
                    showShortToast(mContext, "Order placed successfully")
                } else {
                    showShortToast(mContext, it.message)
                }
            }
        }
    }

    private fun getDataFromSharedPreferences() {
        val preference = application.getSharedPreferences(FoodListActivity.CART, 0x0000)
        val gson = Gson()
        val type: Type = object : TypeToken<MutableList<CartFood>?>() {}.type
        val cartItemString = preference.getString(FoodListActivity.CART_ITEMS, null)
        val cartItemList = cartItemString?.let { gson.fromJson<MutableList<CartFood>>(it, type) } ?: mutableListOf()
        cartFoodListViewModel.cartFoodList.postValue(cartItemList)
    }

    private fun arrangeFood(cartFoodList: MutableList<CartFood>): MutableList<Food> {
        val arrangedFoodList = mutableListOf<Food>()

        // Sorting based on quantity in descending order
        val sortedCartFoods = cartFoodList.sortedByDescending { it.quantity }

        // Adding the sorted food items to arrangedFoodList
        for (cartFood in sortedCartFoods) {
            arrangedFoodList.add(cartFood.food)
        }

        return arrangedFoodList
    }

    private fun calculateTotalAmount(cartFoodList: MutableList<CartFood>): Int {
        return cartFoodList.sumOf { it.quantity * it.food.price!! }
    }
}
