package com.canteenmanagment.ui.FoodList

import android.content.Context
import android.graphics.Canvas
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class BounceEdgeEffect(context: Context) : EdgeEffect(context) {

    private var recyclerView: RecyclerView? = null

    override fun onPull(deltaDistance: Float, displacement: Float) {
        super.onPull(deltaDistance, displacement)
        recyclerView?.let { scaleRecyclerView(it, deltaDistance) }
    }

    override fun onRelease() {
        super.onRelease()
        recyclerView?.let { resetRecyclerViewScale(it) }
    }

    override fun onAbsorb(velocity: Int) {
        super.onAbsorb(velocity)
        recyclerView?.let { scaleRecyclerView(it, velocity / 10000f) }
    }

    private fun scaleRecyclerView(recyclerView: RecyclerView, scaleFactor: Float) {
        recyclerView.scaleX = 1f + abs(scaleFactor)
        recyclerView.scaleY = 1f + abs(scaleFactor)
    }

    private fun resetRecyclerViewScale(recyclerView: RecyclerView) {
        recyclerView.scaleX = 1f
        recyclerView.scaleY = 1f
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }
}

