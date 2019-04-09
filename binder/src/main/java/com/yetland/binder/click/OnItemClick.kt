package com.yetland.binder.click

/**
 *@author YETLAND
 *@date 2019/1/11 11:28
 * 点击事件的操作接口
 */
abstract class OnItemClick {
    abstract fun addObserver(onItemClickObserver: OnItemClickObserver)
    abstract fun addObserver(list: ArrayList<OnItemClickObserver>?)
    abstract fun getObserver(): ArrayList<OnItemClickObserver>?
    abstract fun dispatchObserver(onItemClickObserver: OnItemClickObserver)
    abstract fun dispatchObserver(list: ArrayList<OnItemClickObserver>?)
    abstract fun removeObserver(onItemClickObserver: OnItemClickObserver)
    abstract fun removeAll()
}