package com.hai.yun.cloudonfoot

import android.app.Application
import com.hai.yun.cloudonfoot.daggercompont.DaggerApplicationCompont

class CloudApplication : Application() {

    val appCompont = DaggerApplicationCompont.create()

    override fun onCreate() {
        super.onCreate()
        CloudApplication.myApplication = this;
    }

    fun getApp(): CloudApplication {
        return this
    }

    companion object {
        lateinit var myApplication: CloudApplication
    }


}