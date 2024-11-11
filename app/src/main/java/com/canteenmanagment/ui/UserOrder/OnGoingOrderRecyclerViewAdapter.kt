package com.canteenmanagment.ui.UserOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canteenmanagment.canteen_managment_library.models.Order
import com.canteenmanagment.databinding.ItemOngoingOrderLayoutBinding

class OnGoingOrderRecyclerViewAdapter(
    private val onGoingOrderList: List<Order>,  // Changed to List<Order>
    private val mContext: Context,
    private val openOrderDetailDialog: (orderId: String) -> Unit
) : RecyclerView.Adapter<OnGoingOrderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOngoingOrderLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = onGoingOrderList[position]

        holder.binding.TVOrderId.text = order.id ?: "N/A"
        holder.binding.TVPrice.text = "${calculateTotalAmount(order.foodList ?: mutableListOf())} Birr."
        holder.binding.TVTime.text = (order.time ?: "N/A").toString()

        // Format food list as a string for display
        val foodListString = order.foodList?.joinToString("\n") { cartFood ->
            "${cartFood.food.name} X ${cartFood.quantity}"
        } ?: "No items"
        holder.binding.TVOrderList.text = foodListString

        // Set order status and details with null safety
        if (order.status == Order.Status.READY.value) {
            holder.binding.TVStatus.text = "Ready"
            holder.binding.TVStatus.setTextColor(mContext.resources.getColor(android.R.color.holo_green_dark))
            holder.binding.CL.setOnClickListener {
                openOrderDetailDialog(order.id ?: "")
            }
        } else {
            holder.binding.TVStatus.text = "Preparing"
            holder.binding.TVStatus.setTextColor(mContext.resources.getColor(android.R.color.holo_orange_dark))
        }
    }

    override fun getItemCount(): Int = onGoingOrderList.size

    private fun calculateTotalAmount(cartFoodList: List<com.canteenmanagment.canteen_managment_library.models.CartFood>): Int {
        return cartFoodList.sumOf { it.quantity * (it.food.price ?: 0) }
    }

    class ViewHolder(val binding: ItemOngoingOrderLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
