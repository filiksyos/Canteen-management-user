package com.canteenmanagment.ui.UserOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.canteen_managment_library.models.Order

class UserOrderViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData for ongoing orders
    val userInProgressOrder: LiveData<List<Order>> = liveData {
        FirebaseApiManager.getInProgressOrder().let {
            if (it.isSuccess) emit(it.data as List<Order>)
            else emit(listOf())
        }
    }

    // LiveData for ready orders
    val userReadyOrder: LiveData<List<Order>> = liveData {
        FirebaseApiManager.getReadyOrder().let {
            if (it.isSuccess) emit(it.data as List<Order>)
            else emit(listOf())
        }
    }

    // Calculate the total amount for an order
    fun calculateTotalAmount(cartFoodList: MutableList<CartFood>): Int {
        return cartFoodList.sumOf { it.quantity * (it.food.price ?: 0) }
    }
}
