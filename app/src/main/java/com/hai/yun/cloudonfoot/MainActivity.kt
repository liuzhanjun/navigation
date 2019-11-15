package com.hai.yun.cloudonfoot

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amap.api.col.stln3.it
import com.example.android.navigationadvancedsample.setupWithNavController
import com.hai.yun.cloudonfoot.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //底部导航切换
        if (savedInstanceState == null) {

        }



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
        navgationListener()
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

        val navGraphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_devices,
            R.navigation.nav_warring,
            R.navigation.nav_count,
            R.navigation.nav_person
        )
        val itemIds = listOf(
            R.id.navigation_home,
            R.id.navigation_lists,
            R.id.navigation_warning,
            R.id.navigation_counts,
            R.id.navigation_personal
        )
        val navControllers =
            bottom_nav.setupWithNavController(
                itemIds,
                navGraphIds,
                supportFragmentManager,
                R.id.nav_host_container,
                intent
            )

        navControllers.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })

        currentNavController = navControllers

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

    }

//    override fun onSupportNavigateUp(): Boolean {
//        return currentNavController?.value?.navigateUp() ?: false
//    }
}
