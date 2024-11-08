package com.canteenmanagment.ui.UserOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.canteenmanagment.R
import com.canteenmanagment.databinding.ActivityUserOrderBinding
import com.canteenmanagment.utils.OrderDetailDialog

class UserOrderFragment : Fragment() {

    private lateinit var viewmodel: UserOrderViewModel
    private lateinit var binding: ActivityUserOrderBinding
    private lateinit var orderDetailDialog: OrderDetailDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding for the fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_user_order, container, false)

        // ViewModel initialization
        viewmodel = ViewModelProvider(this).get(UserOrderViewModel::class.java)

        // Initialize orderDetailDialog with simplified parameters
        orderDetailDialog = OrderDetailDialog(requireContext())

        // Observing ongoing orders
        viewmodel.userInProgressOrder.observe(viewLifecycleOwner, Observer { inProgressOrders ->
            binding.RVPreparing.adapter = OnGoingOrderRecyclerViewAdapter(
                inProgressOrders,
                requireContext()
            ) { orderId -> orderDetailDialog.startDialog(orderId) }
        })

        // Observing ready orders
        viewmodel.userReadyOrder.observe(viewLifecycleOwner, Observer { readyOrders ->
            binding.RVReady.adapter = OnGoingOrderRecyclerViewAdapter(
                readyOrders,
                requireContext()
            ) { orderId -> orderDetailDialog.startDialog(orderId) }
        })

        // Returning the root view of the binding
        return binding.root
    }
}
