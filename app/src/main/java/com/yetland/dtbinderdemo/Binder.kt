package com.yetland.dtbinderdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yetland.binder.binder.BaseViewBinder

/**
 *@author YETLAND
 *@date 2019/4/8 11:12
 */
class Binder1 : BaseViewBinder<Bean1, ViewHolder1>() {
    override fun getLayoutId(): Int {
        return R.layout.item_1
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder1 {
        return ViewHolder1(getItemView(inflater, parent))
    }
}

class Binder2 : BaseViewBinder<Bean2, ViewHolder2>() {
    override fun getLayoutId(): Int {
        return R.layout.item_2
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder2 {
        return ViewHolder2(getItemView(inflater, parent))
    }
}