package com.hai.yun.cloudonfoot.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.amap.api.col.stln3.rf
import com.hai.yun.cloudonfoot.R
import java.util.jar.Attributes
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.graphics.RectF
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.RelativeLayout
import android.R.attr.path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.nio.charset.CodingErrorAction.REPLACE
import android.R.attr.path
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class TrapezoidalView : RelativeLayout {
    private var bitmap: Bitmap?=null
    var trapColor: Int = Color.WHITE
    var trapratio: Float = 1f//上下边比例
    val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)


    var mWidth: Int = 0
    var mHeight: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes) {
        val attrs = context.obtainStyledAttributes(attributes, R.styleable.TrapezoidalView, 0, 0)
        trapColor = attrs.getColor(R.styleable.TrapezoidalView_trap_color, Color.WHITE)
        trapratio = attrs.getFloat(R.styleable.TrapezoidalView_ratio_tw_bw, 1f)

        println("trapName=${trapratio}")

        attrs.recycle()



    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        val widthMode = MeasureSpec.getMode(widthMeasureSpec);
        val widthSize = MeasureSpec.getSize(widthMeasureSpec);
        val heightMode = MeasureSpec.getMode(heightMeasureSpec);
        val heightSize = MeasureSpec.getSize(heightMeasureSpec);
        println("heightSize=${heightSize}")
        //给子View传递测量值和测量模式
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        //通过子View计算自己的高度和宽度
        val mWidth_ = MeasureMyWidth()
        val mHeight_ = MeasureMyHeight()

        println("width====${width}")


        //通过测量模式来去确定最终使用哪种宽度和高度
        when (widthMode) {
            //使用子类测量出来的宽度
            MeasureSpec.AT_MOST -> {
                println("==================AT_MOST")
                mWidth = mWidth_
            }
            //使用建议宽度
            MeasureSpec.EXACTLY -> {
                println("==================EXACTLY")
                mWidth = widthSize
            }
            MeasureSpec.UNSPECIFIED -> {
            }
        }

        when (heightMode) {
            MeasureSpec.AT_MOST -> {
                mHeight = mHeight_
            }
            MeasureSpec.EXACTLY -> {
                mHeight = heightSize
            }
            MeasureSpec.UNSPECIFIED -> {
            }
        }

        //设置自己的大小
            setMeasuredDimension(mWidth, mHeight)
        init()
    }

    /**
     * 最宽的子控件作为宽度
     */
    private fun MeasureMyWidth(): Int {
        val childRange: IntRange = 0..childCount - 1
        val size = childRange.foldIndexed(0) { index, acc, value ->
            println("value======${value}")
            val view = getChildAt(value)
            val width = view.measuredWidth + view.marginLeft + view.marginRight
            if (width > acc) {
                width
            } else {
                acc
            }

        }
        return size
    }

    private fun MeasureMyHeight(): Int {
        val childRange: IntRange = 0..childCount - 1
        val size = childRange.foldIndexed(0) { index, acc, value ->
            println("value======${value}")
            val view = getChildAt(value)
            acc + (view.marginTop + view.measuredHeight + view.marginBottom)
        }
        return size
    }

//    //    ViewGroup能够支持子Viewmargin即可 ,使用系统的MarginLayoutParams
//    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
//        return MarginLayoutParams(context, attrs)
//    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0..childCount - 1) {
            val v = getChildAt(i)
            var left_ = v.marginLeft
            var top_ = v.marginTop
            var right_ = left_ + v.measuredWidth

            if (i > 0) {
                val lastView = getChildAt(i - 1)
                top_ = lastView.bottom + lastView.marginBottom + v.marginTop
            }
            var bottom_ = top_ + v.measuredHeight
            v.layout(left_, top_, right_, bottom_)
        }

    }

    fun init(){
        setLayerType(LAYER_TYPE_HARDWARE,null);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.setDither(true);
        //加快显示速度，本设置项依赖于dither和xfermode的设置
        paint.setFilterBitmap(true);
        bitmap= Bitmap.createBitmap(mWidth,mHeight,Bitmap.Config.ARGB_8888)

    }

    override fun dispatchDraw(canvas: Canvas?) {

        val martrix:Matrix=Matrix()
        canvas?.concat(martrix)
        super.dispatchDraw(canvas)

    }
}