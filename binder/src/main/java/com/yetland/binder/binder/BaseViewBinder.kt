package com.yetland.binder.binder

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yetland.binder.click.OnItemClick
import com.yetland.binder.click.OnItemClickOwner
import com.yetland.binder.click.OnItemClickRegistry
import com.yetland.binder.holder.BaseViewHolderWrapper
import me.drakeet.multitype.ItemViewBinder

/**
 *@author YETLAND
 *@date 2019/1/11 14:49
 */
@Suppress("LeakingThis")
abstract class BaseViewBinder<T, R : BaseViewHolderWrapper<T>> : ItemViewBinder<T, R>(), OnItemClickOwner {

    private val onItemRegistry = OnItemClickRegistry(this)

    override fun getOnItemClick(): OnItemClick {
        return onItemRegistry
    }

    @LayoutRes
    private val layoutId: Int

    /**
     * viewHolder layout
     *
     * @return layout id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    init {
        layoutId = this.getLayoutId()
    }

    fun getItemView(inflater: LayoutInflater, parent: ViewGroup): View {
        if (layoutId == 0) {
            throw IllegalArgumentException("You must return a layout id.")
        }
        return inflater.inflate(layoutId, parent, false)
    }

    override fun onBindViewHolder(holder: R, item: T) {
        holder.getOnItemClick().dispatchObserver(getOnItemClick().getObserver())
        holder.setData(item)
    }
}
