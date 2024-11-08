package com.canteenmanagment.ui.UserOrder

import android.content.Context
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

        // Initialize orderDetailDialog
        orderDetailDialog = OrderDetailDialog(requireContext())

        // Observing userPastOrderList and setting adapter
        viewmodel.userPastOrderList.observe(viewLifecycleOwner, Observer {
            binding.RVPastOrder.adapter = PastOrderRecyclerViewAdapter(
                viewmodel.getHashmapFromList(it)
            )
        })

        // Observing userInProgressOrder and setting adapter
        viewmodel.userInProgressOrder.observe(viewLifecycleOwner, Observer {
            binding.RVPreparing.adapter = OnGoingOrderRecyclerViewAdapter(
                viewmodel.getPastOrderListFromOrderList(it),
                requireContext()
            ) { orderId, transactionId -> orderDetailDialog.startDialog(orderId, transactionId) }
        })

        // Observing userReadyOrder and setting adapter
        viewmodel.userReadyOrder.observe(viewLifecycleOwner, Observer {
            binding.RVReady.adapter = OnGoingOrderRecyclerViewAdapter(
                viewmodel.getPastOrderListFromOrderList(it),
                requireContext()
            ) { orderId, transactionId -> orderDetailDialog.startDialog(orderId, transactionId) }
        })

        // Returning the root view of the binding
        return binding.root
    }
}
