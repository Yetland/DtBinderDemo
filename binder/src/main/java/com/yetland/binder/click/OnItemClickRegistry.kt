package com.yetland.binder.click

import java.lang.ref.WeakReference

/**
 *@author YETLAND
 *@date 2019/1/11 12:19
 * 点击事件的操作者，也是注册中心，来处理事件的分发等
 */
class OnItemClickRegistry() : OnItemClick() {

    /**
     * 暂时没用到，因为现在都是直接点击回调，是实时的，不是异步，也不存在什么生命周期的问题
     */
    private lateinit var mLifecycleOwner: WeakReference<OnItemClickOwner>
    /**
     * 所有observer的集合
     */
    private var mObserverList = arrayListOf<OnItemClickObserver>()

    constructor(owner: OnItemClickOwner) : this() {
        mLifecycleOwner = WeakReference(owner)
    }

    /**
     * 分发observer，其实就是add，只是区分一下功能
     */
    override fun dispatchObserver(onItemClickObserver: OnItemClickObserver) {
        addObserver(arrayListOf(onItemClickObserver))
    }

    override fun dispatchObserver(list: ArrayList<OnItemClickObserver>?) {
        addObserver(list)
    }

    override fun getObserver(): ArrayList<OnItemClickObserver>? {
        return mObserverList
    }

    override fun addObserver(list: ArrayList<OnItemClickObserver>?) {
        list?.forEach { after ->
            if (!mObserverList.contains(after)) {
                mObserverList.add(after)
            }
        }
    }

    override fun addObserver(onItemClickObserver: OnItemClickObserver) {
        addObserver(arrayListOf(onItemClickObserver))
    }

    override fun removeObserver(onItemClickObserver: OnItemClickObserver) {
        mObserverList.remove(onItemClickObserver)
    }

    override fun removeAll() {
        mObserverList.clear()
    }
}