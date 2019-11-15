package com.hai.yun.cloudonfoot.views

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.text.TextPaint


open class CustomView : View {
    //view的上下文
    protected var mCurrentContext: Context
    /**
     * view的宽度
     */
    protected var mViewWidth: Int = 0

    /**
     * view的高度
     */
    protected var mViewHeight: Int = 0
    /**
     * 默认画笔
     */
    protected var mDefaultPaint: Paint

    /**
     * 默认文字画笔
     */
    protected var mDefaultTextPaint: TextPaint


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        mCurrentContext = context
    }

    init {
        mDefaultPaint = Paint()
        mDefaultPaint.setColor(Color.BLACK)
        mDefaultPaint.style = Paint.Style.STROKE
        mDefaultPaint.strokeWidth = 1f
        mDefaultPaint.isAntiAlias = true
        mDefaultTextPaint = TextPaint()
        mDefaultTextPaint.textSize = 20f
        mDefaultTextPaint.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mViewWidth = w
        mViewHeight = h
    }
}


