package com.kjk.marsrealestate.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.marsrealestate.databinding.ListItemMarsEstateBinding
import com.kjk.marsrealestate.network.MarsProperty

class MarsRealEstateAdapter(
    private val clickListener: OnClickListener
) : ListAdapter<MarsProperty, MarsRealEstateAdapter.MarsRealEstateViewHolder>(MarsPropertyDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsRealEstateViewHolder {
        return MarsRealEstateViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MarsRealEstateViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    // ViewHolder Class
    class MarsRealEstateViewHolder(
        private val binding: ListItemMarsEstateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(property: MarsProperty, clickListener: OnClickListener) {
            binding.marsProperty = property
            binding.clickListener = clickListener
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

/** DiffUtil */
class MarsPropertyDiffUtilCallBack : DiffUtil.ItemCallback<MarsProperty>() {
    override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem.id == newItem.id
    }
}

/** click Listener */
class OnClickListener(private val clickListener: (marsProperty : MarsProperty) -> Unit) {
    fun onClickListener(marsProperty: MarsProperty) = clickListener(marsProperty)
}