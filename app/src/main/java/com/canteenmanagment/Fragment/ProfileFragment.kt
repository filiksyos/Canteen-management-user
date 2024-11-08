package com.canteenmanagment.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.canteenmanagment.MainActivity
import com.canteenmanagment.databinding.FragmentProfileBinding
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        FirebaseAuth.getInstance().currentUser.let { firebaseUser ->
            binding.TVEmail.text = firebaseUser?.email
            binding.TVName.text = firebaseUser?.displayName
        }

        binding.TVLogout.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val i = Intent(activity?.applicationContext, MainActivity::class.java)

            startActivity(i)
            activity?.finish()
        }

        // Removed TVMyOrder click listener as MyOrder is now handled in HomeActivity
        val versionName = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionName
        binding.TVVersion.text = "V$versionName"

        binding.TVFav.setOnClickListener {
            val i = Intent(context, FoodListActivity::class.java)
            i.putExtra(FAVOURITE, true)
            startActivity(i)
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val FAVOURITE = "Favourite"
    }
}
