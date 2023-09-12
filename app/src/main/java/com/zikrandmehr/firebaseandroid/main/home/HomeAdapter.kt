/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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