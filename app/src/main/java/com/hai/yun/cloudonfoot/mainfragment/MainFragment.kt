package com.hai.yun.cloudonfoot.mainfragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle
import com.hai.yun.cloudonfoot.CloudApplication

import com.hai.yun.cloudonfoot.R
import com.hai.yun.cloudonfoot.map.LifeMapView
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MainFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mapView: LifeMapView
    private var aMap: AMap? = null
    @Inject
    lateinit var myLocationStyle: MyLocationStyle
    @Inject
    lateinit var aMapLocationClient: AMapLocationClient

    @Inject
    lateinit var mLocationOption: AMapLocationClientOption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        CloudApplication.myApplication.appCompont.inject(this)
        println("================onCreate" + savedInstanceState)


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        println("================onCreateView" + savedInstanceState)
        mapView = view.findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        lifecycle.addObserver(mapView)

        if (aMap == null) {
            aMap = mapView.map
        }
        println("=============" + myLocationStyle)
        //添加定位风格，定位蓝点，定位间隔时间，是否连续定位等等
        //DaggerModules.provideMyLocationStyle提供注入
//        aMap?.setMyLocationStyle(myLocationStyle)
        //是否显示定位按钮
//        aMap?.uiSettings!!.isMyLocationButtonEnabled = true
//        aMap?.isMyLocationEnabled=true
//        mapLocationListener(aMap)

        //设置定位参数
        aMapLocationClient.setLocationOption(mLocationOption)

        //设置监听回调
        aMapLocationClient.setLocationListener(AMapLocationListener {
            println("bbbb=====${it.longitude}=========${it.latitude}")
        })

        //启动定位
        aMapLocationClient.startLocation();


        return view
    }


    fun mapLocationListener(aMap: AMap?) {
        aMap?.setOnMyLocationChangeListener {
            println("=====${it.longitude}=========${it.latitude}")
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("===========onAttach")
    }

    override fun onDetach() {
        super.onDetach()

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
