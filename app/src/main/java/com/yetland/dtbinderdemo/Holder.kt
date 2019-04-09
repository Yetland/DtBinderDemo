package com.yetland.dtbinderdemo

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.yetland.binder.holder.BaseViewHolderWrapper

/**
 *@author YETLAND
 *@date 2019/4/8 11:12
 */
class ViewHolder1(itemView: View) : BaseViewHolderWrapper<Bean1>(itemView) {
    private val textView = itemView.findViewById<TextView>(R.id.textView)
    override fun setData(t: Bean1) {
        super.setData(t)
        textView.text = t.name
    }

    override fun onClick(v: View) {
        getObserver(Observer1::class.java)?.onClick1()
    }
}

class ViewHolder2(itemView: View) : BaseViewHolderWrapper<Bean2>(itemView) {
    var recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView2)
    private val adapter = RecyclerViewAdapter2(mContext)

    init {
        recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.submitList(listOf(
            Bean1("第二层"),
            Bean1("第二层"),
            Bean1("第二层")
        ))
    }

    override fun setData(t: Bean2) {
        super.setData(t)
        adapter.getOnItemClick().dispatchObserver(getOnItemClick().getObserver())
    }

    override fun onClick(v: View) {
        getObserver(Observer2::class.java)?.onClick2()
    }
}