package com.kjk.trackmysleepquality.sleeptracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.trackmysleepquality.R
import com.kjk.trackmysleepquality.database.SleepNight
import com.kjk.trackmysleepquality.databinding.ItemListSleepNightBinding
import convertDurationToFormatted
import convertNumericQualityToString

/**
 * DiffUtil을 사용한다.
 */
class SleepNightDiffCallBack : DiffUtil.ItemCallback<SleepNight>() {

    // item의 id를 비교한다.
    // id가 같지 않다면, 변경되었다는 의미이다. (추가 혹은 삭제)
    // areItemsTheSame은 log를 찍어보면, 2번 순회해 체크를 한다.
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    // item의 content를 비교한다.
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.SleepNightViewHolder>(SleepNightDiffCallBack()) /* RecyclerView.Adapter<SleepNightAdapter.SleepNightViewHolder>()*/ {

    /* DiffUtil을 사용하므로 필요없다.
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        val binding = ItemListSleepNightBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SleepNightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SleepNightViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.bind(getItem(position))
    }

    /** DiffUtil을 사용하므로, 필요없다.
    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${data.size}")
        return data.size
    }
    */
    
    companion object {
        private const val TAG = "SleepNightAdapter"
    }

    // ViewHolder Class
    class SleepNightViewHolder(
        private val binding: ItemListSleepNightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /** Binding Adapter를 사용하므로, 직접 view의 객체를 가져와 set하지 않는다.*/
        fun bind(sleepNight: SleepNight) {
//            setImageSource(sleepNight)
//            setSleepLengthTextView(sleepNight)
//            setSleepQualityTextView(sleepNight)

            /** binding adapter 사용한다. */
            binding.sleepNight = sleepNight
        }

        /*
        private fun setImageSource(sleepNight: SleepNight) {
            imageView.setImageResource(getImageSource(sleepNight.sleepQuality))
        }

        private fun getImageSource(quality: Int): Int {
            return when(quality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            }
        }

        private fun setSleepLengthTextView(sleepNight: SleepNight) {
            sleepLengthTextView.text = convertDurationToFormatted(
                sleepNight.startTimeMillis,
                sleepNight.endTimeMillis,
                res
            )
        }

        private fun setSleepQualityTextView(sleepNight: SleepNight) {
            sleepQualityTextView.text = convertNumericQualityToString(
                sleepNight.sleepQuality,
                res
            )
        }
         */
    }
}
