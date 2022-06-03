package com.kjk.trackmysleepquality

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kjk.trackmysleepquality.database.SleepNight
import convertDurationToFormatted
import convertNumericQualityToString

@BindingAdapter("sleepQualityImage")
fun ImageView.setSleepQualityImage(sleepNight: SleepNight) {
    setImageResource(when(sleepNight.sleepQuality) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_sleep_active
    })
}

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(sleepNight: SleepNight) {
    text = convertDurationToFormatted(
        sleepNight.startTimeMillis,
        sleepNight.endTimeMillis,
        context.resources
    )
}

@BindingAdapter("sleepQualityFormatted")
fun TextView.setSleepQualityFormatted(sleepNight: SleepNight) {
    text = convertNumericQualityToString(
        sleepNight.sleepQuality,
        context.resources
    )
}