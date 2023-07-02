package com.zikrandmehr.firebaseandroid.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zikrandmehr.firebaseandroid.databinding.HomeViewHolderBinding

class HomeItem(
    val imageView: Int,
    val title: CharSequence,
    val description: CharSequence,
    val onClick: () -> Unit
)

class HomeAdapter(private val items: List<HomeItem>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeViewHolderBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    inner class HomeViewHolder(
        private val binding: HomeViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeItem) {
            binding.apply {
                ivFunction.setImageResource(item.imageView)
                tvTitle.text = item.title
                tvDescription.text = item.description
                root.setOnClickListener { item.onClick.invoke() }
            }
        }
    }
}