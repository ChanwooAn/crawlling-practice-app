package com.bed.android.bedrock.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.R
import com.bumptech.glide.Glide

class CardStackAdapter(private var spots: List<String> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.stack_item_productimage, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position % spots.size]
        Glide.with(holder.image)
            .load(spot)
            .into(holder.image)
        Log.d("CardAdapter", "onbind" + spot)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun setSpots(spots: List<String>) {
        this.spots = spots
    }

    fun getSpots(): List<String> {
        return spots
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.product_image)
    }

}
