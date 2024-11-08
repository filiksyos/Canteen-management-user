package com.canteenmanagment.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.canteenmanagment.R
import com.canteenmanagment.databinding.OrderDetailDiaologBoxBinding

class OrderDetailDialog(val context: Context) {

    private lateinit var alertDialog: Dialog
    private lateinit var binding: OrderDetailDiaologBoxBinding

    fun startDialog(orderDetails: String) {
        val dialog = AlertDialog.Builder(context)

        // Initialize ViewBinding
        binding = OrderDetailDiaologBoxBinding.inflate(LayoutInflater.from(context))
        val view = binding.root

        dialog.setView(view)

        // Set order details
        binding.TVOrderDetails.text = orderDetails

        // Show dialog
        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun stopDialog() {
        if (::alertDialog.isInitialized && alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }
}
