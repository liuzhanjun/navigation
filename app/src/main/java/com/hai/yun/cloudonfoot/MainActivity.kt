package com.hai.yun.cloudonfoot

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amap.api.col.stln3.it
import com.hai.yun.cloudonfoot.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = nav_host_fragment.findNavController()
        bottom_nav.setupWithNavController(navController)
        //底部导航切换
        navgationListener()


        requestPermisses()
    }

    fun requestPermisses() {
        PermissionUtils.needPermission(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE

            )
        )
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        println("====================" + requestCode)
//        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults,
            success = {
                Toast.makeText(this, "请求成功", Toast.LENGTH_SHORT).show()
            },
            fail = {
                Toast.makeText(this, "${it}请求失败", Toast.LENGTH_SHORT).show()
            })

    }


    private fun navgationListener() {

        bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.mainFragment)
                    true
                }
                R.id.navigation_lists -> {
                    navController.navigate(R.id.deviceListFragment)
                    true
                }
                R.id.navigation_warning -> {
                    navController.navigate(R.id.warningListFragment)
                    true
                }
                R.id.navigation_counts -> {
                    navController.navigate(R.id.countFragment)
                    true
                }
                R.id.navigation_personal -> {
                    navController.navigate(R.id.personalFragment)
                    true
                }
                else -> false
            }
        }

    }
}
