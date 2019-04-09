package com.yetland.binder.holder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yetland.binder.click.OnItemClickOwner

/**
 *@author YETLAND
 *@date 2019/1/11 14:53
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), OnItemClickOwner {
    var mT: T? = null
    var mContext: Context = itemView.context

    /**
     * 设置数据
     *
     * @param t 数据类型T
     */
    abstract fun setData(t: T)
}
