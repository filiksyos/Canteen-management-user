package com.canteenmanagment.ui.FoodList

import android.content.Context
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView

class BounceEdgeEffectFactory(private val context: Context) : RecyclerView.EdgeEffectFactory() {

    override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect {
        val bounceEdgeEffect = BounceEdgeEffect(context)
        bounceEdgeEffect.setRecyclerView(recyclerView) // Set RecyclerView to modify during pull
        return bounceEdgeEffect
    }
}
