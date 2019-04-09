package com.yetland.binder.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yetland.binder.click.OnItemClick
import com.yetland.binder.click.OnItemClickOwner
import com.yetland.binder.click.OnItemClickRegistry
import com.yetland.binder.holder.BaseViewHolderWrapper

/**
 *@author YETLAND
 *@date 2019/3/22 15:08
 */
abstract class BaseRecyclerViewAdapter<T>(private val context: Context)
    : RecyclerView.Adapter<BaseViewHolderWrapper<T>>(),
        OnItemClickOwner {
    private val registry = OnItemClickRegistry(this)

    override fun getOnItemClick(): OnItemClick {
        return registry
    }

    /**
     * 通过[layoutId]获取itemView
     */
    fun getItemView(layoutId: Int, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(layoutId, parent, false)
    }

    override fun onBindViewHolder(holder: BaseViewHolderWrapper<T>, position: Int) {
        // 这是给holder添加一个OnViewHolderClickObserver
        holder.getOnItemClick().dispatchObserver(getOnItemClick().getObserver())
    }
}