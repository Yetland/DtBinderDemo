package com.yetland.dtbinderdemo

import com.yetland.binder.click.OnItemClickObserver

/**
 *@author YETLAND
 *@date 2019/4/8 11:13
 */
interface Observer1 : OnItemClickObserver {
    fun onClick1()
}

interface Observer2 : OnItemClickObserver {
    fun onClick2()
}