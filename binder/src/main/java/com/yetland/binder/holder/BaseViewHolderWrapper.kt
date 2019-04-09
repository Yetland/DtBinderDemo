package com.yetland.binder.holder

import android.support.annotation.NonNull
import android.view.View
import com.yetland.binder.click.OnItemClick
import com.yetland.binder.click.OnItemClickObserver
import com.yetland.binder.click.OnItemClickRegistry

/**
 *@author YETLAND
 *@date 2019/1/11 14:56
 */
open class BaseViewHolderWrapper<T>(itemView: View) :
        BaseViewHolder<T>(itemView), View.OnClickListener {

    private val onItemRegistry = OnItemClickRegistry(this)

    override fun setData(@NonNull t: T) {
        mT = t
        itemView?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
    }

    fun <R : OnItemClickObserver> getObserver(clazz: Class<R>): R? {
        getOnItemClick().getObserver()?.forEach {
            if (clazz.isInstance(it)) {
                @Suppress("UNCHECKED_CAST")
                return it as R
            }
        }
        return null
    }


    override fun getOnItemClick(): OnItemClick {
        return onItemRegistry
    }
}