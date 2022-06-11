package com.kjk.marsrealestate.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kjk.marsrealestate.databinding.ListItemMarsEstateBinding
import com.kjk.marsrealestate.network.MarsProperty

class MarsRealEstateAdapter :
    RecyclerView.Adapter<MarsRealEstateAdapter.MarsRealEstateViewHolder>() {

    var properties = listOf<MarsProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsRealEstateViewHolder {
        return MarsRealEstateViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MarsRealEstateViewHolder, position: Int) {
        holder.bind(properties[position])
    }

    override fun getItemCount() = properties.size

    fun updateProperties(properties: List<MarsProperty>) {
        this.properties = properties
    }

    // ViewHolder Class
    class MarsRealEstateViewHolder(
        private val binding: ListItemMarsEstateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(property: MarsProperty) {
            binding.marsProperty = property
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MarsRealEstateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMarsEstateBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return MarsRealEstateViewHolder(binding)
            }
        }
    }
}