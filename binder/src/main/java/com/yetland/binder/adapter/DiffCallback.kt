package com.yetland.binder.adapter

import android.support.v7.util.DiffUtil

/**
 *@author YETLAND
 *@date 2019/3/22 15:05
 */
open class DiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}