package com.hai.yun.cloudonfoot.daggercompont

import com.hai.yun.cloudonfoot.MainActivity
import com.hai.yun.cloudonfoot.daggermodule.DaggerModules
import com.hai.yun.cloudonfoot.mainfragment.MainFragment
import dagger.Component


@Component(modules = [DaggerModules::class])
interface ApplicationCompont {
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
}