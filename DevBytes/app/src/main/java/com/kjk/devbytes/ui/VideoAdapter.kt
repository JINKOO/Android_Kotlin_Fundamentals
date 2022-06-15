package com.kjk.devbytes.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjk.devbytes.databinding.ItemDevbyteBinding
import com.kjk.devbytes.domain.DevByteVideo

class VideoAdapter : ListAdapter<DevByteVideo, VideoAdapter.VideoViewHolder>(VideoDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VideoViewHolder(
        private val binding: ItemDevbyteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(devByteVideo: DevByteVideo) {
            // binding adapter를 사용해야 한다.
            binding.devbytevideo = devByteVideo
            
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): VideoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDevbyteBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return VideoViewHolder(binding)
            }
        }
    }
}

class VideoDiffUtilCallBack : DiffUtil.ItemCallback<DevByteVideo>() {
    /**
     * 동일한 객체인지 판별한다.
     * 여기서는 url이 PK이다.
     * */
    override fun areItemsTheSame(oldItem: DevByteVideo, newItem: DevByteVideo): Boolean {
        return oldItem.url == newItem.url
    }

    /** areItemsTheSame()에서 true를 반환할 때에만, 이 함수가 실행된다.
     * 객체가 같을 때에만, content도 비교한다.*/
    override fun areContentsTheSame(oldItem: DevByteVideo, newItem: DevByteVideo): Boolean {
        return oldItem == newItem
    }
}