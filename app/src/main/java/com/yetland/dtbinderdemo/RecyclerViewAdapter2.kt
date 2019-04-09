package com.yetland.dtbinderdemo

import android.content.Context
import android.view.ViewGroup
import com.yetland.binder.adapter.BaseListAdapter
import com.yetland.binder.adapter.DiffCallback
import com.yetland.binder.holder.BaseViewHolderWrapper

/**
 *@author YETLAND
 *@date 2019/4/8 11:13
 */
class RecyclerViewAdapter2(context: Context) : BaseListAdapter<Bean1>(context, DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWrapper<Bean1> {
        return ViewHolder1(getItemView(R.layout.item_1, parent))
    }
}