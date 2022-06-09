package com.kjk.trackmysleepquality.sleeptracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.trackmysleepquality.R
import com.kjk.trackmysleepquality.database.SleepNight
import com.kjk.trackmysleepquality.databinding.HeaderBinding
import com.kjk.trackmysleepquality.databinding.ItemListSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

/** recyclerview에서 header 지정 */
private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

private val adapterScope = CoroutineScope(Dispatchers.Default)

class SleepNightAdapter(
    private val clickListener: SleepNightClickListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallBack())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> SleepNightViewHolder.from(parent)
            else -> {
                throw ClassCastException("Unknown Class ViewType :: ${viewType}")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        //holder.bind(getItem(position))
        when (holder) {
            is SleepNightViewHolder -> {
                val sleepNightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(sleepNightItem.sleepNight, clickListener)
            }
        }
    }

    /** Item view type을 구분하는 함수 */
    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.HeaderItem -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.HeaderItem)
                else -> listOf(DataItem.HeaderItem) + list.map {
                    DataItem.SleepNightItem(it)
                }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
    
    companion object {
        private const val TAG = "SleepNightAdapter"
    }

    // Header ViewHolderClass
    class TextViewHeaderViewHolder(
        private val binding: HeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): TextViewHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return TextViewHeaderViewHolder(binding)
            }
        }
    }

    // ViewHolder Class
    class SleepNightViewHolder(
        private val binding: ItemListSleepNightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /** Binding Adapter를 사용하므로, 직접 view의 객체를 가져와 set하지 않는다.*/
        fun bind(sleepNight: SleepNight, clickListener: SleepNightClickListener) {
            /** binding adapter 사용한다. */
            binding.sleepNight = sleepNight
            /** click Listener set*/
            binding.sleepNightClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SleepNightViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListSleepNightBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return SleepNightViewHolder(binding)
            }
        }
    }
}

/**
 * DiffUtil을 사용한다.
 */
class SleepNightDiffCallBack : DiffUtil.ItemCallback<DataItem>() {

    // item의 id를 비교한다.
    // id가 같지 않다면, 변경되었다는 의미이다. (추가 혹은 삭제)
    // areItemsTheSame은 log를 찍어보면, 2번 순회해 체크를 한다.
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.sleepNightId == newItem.sleepNightId
    }

    // item의 content를 비교한다.
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

/** Click Listener */
class SleepNightClickListener(val clickListener: (sleepNightKey: Long) -> Unit) {
    fun onClick(sleepNight: SleepNight) = clickListener(sleepNight.nightId)
}

/** Sealed Class */
sealed class DataItem {

    abstract val sleepNightId: Long

    /** SleepNight Item */
    data class SleepNightItem(
        val sleepNight: SleepNight
        ) : DataItem() {
        override val sleepNightId: Long = sleepNight.nightId
    }

    /** Header Item */
    object HeaderItem : DataItem() {
        override val sleepNightId: Long = Long.MIN_VALUE
    }
}