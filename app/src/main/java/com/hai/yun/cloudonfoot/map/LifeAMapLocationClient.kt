package com.hai.yun.cloudonfoot.map

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.amap.api.location.AMapLocationClient

class LifeAMapLocationClient : AMapLocationClient, LifecycleObserver {

    constructor(context: Context) : super(context)
    constructor(var1: Context, var2: Intent) : super(var1, var2)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun liftStartLocation() {
        //开始定位
        this.startLocation()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun lifeStop() {
        //停止定位
        this.stopLocation()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun lifeDestroy() {
        //销毁
        this.onDestroy()
    }
}