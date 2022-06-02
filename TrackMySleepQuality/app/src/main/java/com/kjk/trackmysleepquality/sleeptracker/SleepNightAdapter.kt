package com.kjk.trackmysleepquality.sleeptracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kjk.trackmysleepquality.R
import com.kjk.trackmysleepquality.database.SleepNight
import convertDurationToFormatted
import convertNumericQualityToString

class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.SleepNightViewHolder>() {

    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        val layoutInflater = LayoutInflater.from(parent.context)
        val view  = layoutInflater.inflate(
            R.layout.item_list_sleep_night,
            parent,
            false
        )
        return SleepNightViewHolder(view)
    }

    override fun onBindViewHolder(holder: SleepNightViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${data.size}")
        return data.size
    }
    
    companion object {
        private const val TAG = "SleepNightAdapter"
    }

    // ViewHolder Class
    class SleepNightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById<ImageView>(R.id.sleep_quality_image)
        val sleepLengthTextView = itemView.findViewById<TextView>(R.id.sleep_length_text_view)
        val sleepQualityTextView = itemView.findViewById<TextView>(R.id.sleep_quality_text_view)
        val res = itemView.context.resources

        fun bind(sleepNight: SleepNight) {
            setImageSource(sleepNight)
            setSleepLengthTextView(sleepNight)
            setSleepQualityTextView(sleepNight)
        }

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
    }
}
