package com.hai.yun.cloudonfoot.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils {

    companion object X {
        private var requestCode: Int = 0
        private lateinit var permissions: MutableList<String>
        private lateinit var context: Activity
        private fun with(context: Activity): X {
            this.context = context
            return this
        }

        fun needPermission(context: Activity, array: Array<String>) {
            PermissionUtils.with(context).let {
                if (it.checkVersion()) {
                    it.addRequestCode(200)
                    it.addPermission(array)
                    it.request()
                }
            }
        }

        private fun checkVersion(): Boolean {
            return Build.VERSION.SDK_INT > Build.VERSION_CODES.M
        }

        private fun addRequestCode(requestCode_: Int) {
            this.requestCode = requestCode_
        }

        private fun addPermission(permissions_: Array<String>) {
            permissions = mutableListOf()
            permissions.addAll(permissions_)
        }

        private fun request(): X {
            val permissionTemp = arrayListOf<String>()
            permissions.forEach() { permission ->
                //判断该权限是否已通过
                val checked: Int = ContextCompat.checkSelfPermission(context, permission)
                println("=========checked=${checked}===========${permission}")
                if (checked == PackageManager.PERMISSION_DENIED) {
                    permissionTemp.add(permission)
                }
            }
            if (!permissionTemp.isNullOrEmpty()) {
                ActivityCompat.requestPermissions(context, permissionTemp.toTypedArray(), requestCode)
            }
            return this;
        }

        fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>,
            grantResults: IntArray,
            success: () -> Unit,
            fail: (permission: List<String>) -> Unit
        ) {

            if (grantResults.size == 0) {
                return
            }
            val indexs = mutableListOf<Int>()
            grantResults.forEachIndexed { index, result ->
                if (result == PackageManager.PERMISSION_DENIED) {
                    indexs.add(index)
                }

            }
            val x = permissions.filterIndexed { index, s ->
                index in indexs
            }
            if (!x.isNullOrEmpty()) {
                fail(x)
            } else {
                success()
            }

        }
    }


}