package com.hai.yun.cloudonfoot.daggermodule

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.model.MyLocationStyle
import com.hai.yun.cloudonfoot.CloudApplication
import dagger.Module
import dagger.Provides

@Module
class DaggerModules {


    @Provides
    fun provideMyLocationStyle(): MyLocationStyle {
        //初始化定位蓝点样式类
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，
        // 并且会跟随设备移动。（1秒1次定位）
        // 如果不设置myLocationType，默认也会执行此种模式。
        val myLocationStyle = MyLocationStyle()
        //连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
        //设置连续定位模式下的定位间隔，
        // 只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(1000);
//        设置是否显示定位小蓝点，
//        用于满足只想使用定位，
//        不想使用定位小蓝点的场景，
//        设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(true)
        return myLocationStyle
    }

    @Provides
    fun providerAMapLocationClient(): AMapLocationClient {
        return AMapLocationClient(CloudApplication.myApplication.getApp())
    }


    @Provides
    fun providerAMapLocationClientOption(): AMapLocationClientOption {
        val mLocationOption = AMapLocationClientOption()
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置持续定位间隔
        mLocationOption.setInterval(1000);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        return mLocationOption
    }
}