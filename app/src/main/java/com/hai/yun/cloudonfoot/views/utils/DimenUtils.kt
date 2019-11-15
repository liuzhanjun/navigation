package com.lzj.kotlinandroidnotes.views.utils

import android.content.Context
import android.util.DisplayMetrics
import java.text.DecimalFormat

class DimenUtils {
    companion object Instance {
        private lateinit var displayMetrics: DisplayMetrics

        fun getContext(context: Context) {
            displayMetrics = context.resources.displayMetrics
        }

        //获得屏幕的像素宽度单位px
        fun getScreenWidthPx(): Int {
            return displayMetrics.widthPixels
        }

        //获得屏幕的像素高度
        fun getScreenHeightPx(): Int {
            return displayMetrics.heightPixels
        }

        //获得屏幕的单位dp
        fun getScreenWidthDp(): Float {
            return getPxMapDp(displayMetrics.widthPixels)
        }

        //获得屏幕的高度
        fun getScreenHeightDp(): Float {
            return getPxMapDp(displayMetrics.heightPixels)
        }

        //获得屏幕的逻辑尺寸
        fun getDimen(): Float {

            //获得分辨率
            val widthpx = displayMetrics.widthPixels
            val heightpx = displayMetrics.heightPixels
            //获得dpi
            val densitydip = displayMetrics.densityDpi
            //获得像素比例
            val density = displayMetrics.density
            //获得逻辑尺寸
            //计算公式逻辑尺寸=sqrt(w*w+h*h)/屏幕密度dpi
            val zh = Math.sqrt((widthpx * widthpx + heightpx * heightpx).toDouble())
            return getTwoDecimal(zh / densitydip)
        }

        /**
         * 获得dp对应的px
         *
         * @param dp dp
         * @return
         */
        fun getDpMapPx(dp: Int): Float {
            //获得
            return dp * displayMetrics.density
        }

        /**
         * 获得px对应的dp
         *
         * @param px dp
         * @return
         */
        fun getPxMapDp(px: Int): Float {
            //获得
            return px / displayMetrics.scaledDensity
        }

        /**
         * 获得sp对应的px
         */
        fun getSpMapPx(sp: Int): Float {
            return sp * displayMetrics.density
        }

        fun getTwoDecimal(decimal: Double): Float {
            val format = DecimalFormat("0.00")
            return java.lang.Float.parseFloat(format.format(decimal))
        }
    }

}