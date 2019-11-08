package com.hai.yun.cloudonfoot.map

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView

class LifeMapView : MapView, LifecycleObserver {

    constructor(var1: Context) : super(var1) {}
    constructor(var1: Context, var2: AttributeSet) : super(var1, var2)
    constructor(var1: Context, var2: AttributeSet, var3: Int) : super(var1, var2, var3)
    constructor(var1: Context, var2: AMapOptions) : super(var1, var2)


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun lifeOnResume() {
        this.onResume()
        println("==============onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun lifeOnPause() {
        this.onPause()
        println("==============pause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun lifeOnDestroy() {
        this.onDestroy()
    }

}