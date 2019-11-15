package com.hai.yun.cloudonfoot.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.graphics.contains
import com.lzj.kotlinandroidnotes.views.utils.DimenUtils
import kotlin.math.absoluteValue
import androidx.core.view.ViewCompat.getMatrix
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.graphics.withMatrix
import com.hai.yun.cloudonfoot.R
import com.hai.yun.cloudonfoot.views.utils.CanvasAidUtils
import java.util.*


class RemoteControlMenu : CustomView {

    var isShowAxis = false
    //中间圆的半径 单位dp
    var centerCirclerRadius: Float = 0f
    //扇形的厚度dp
    var arcHight: Float = 0f
        set(value) {
            field = dp2px(value)
        }
    //大扇形半径
    private var bigArcRadius: Float = 0f
    //小扇形半径
    private var smallArcRadius: Float = 0f
    //dp
    var interval: Float = 0f
        set(value) {
            field = dp2px(value)
        }
    //中间圆
    private var centerCirclerPath: Path
    //上边扇形
    private var topArcPath: Path

    //下边扇形
    private var bottomArcPath: Path
    private var leftArcPath: Path
    private var rightArcPath: Path

    private lateinit var bigrectF: RectF
    private lateinit var smallrectF: RectF

    //中心圆的范围
    private var centerRegion: Region
    private lateinit var clipRegion: Region

    private var touchPoint: FloatArray

    private var canvasMatrix: Matrix

    var backColor: Int = 0

    init {
        centerCirclerPath = Path()
        topArcPath = Path()
        bottomArcPath = Path()
        leftArcPath = Path()
        rightArcPath = Path()
        touchPoint = floatArrayOf(-1f, -1f)
        centerRegion = Region()
        canvasMatrix = Matrix()

    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val type = context.obtainStyledAttributes(attributeSet, R.styleable.RemoteControlMenu)
        backColor = type.getColor(R.styleable.RemoteControlMenu_backColor, Color.parseColor("#4D5266"))
        interval = type.getDimension(R.styleable.RemoteControlMenu_interval, 0f)
        arcHight = type.getDimension(R.styleable.RemoteControlMenu_arcHight, 0f)
        type.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var len = mViewWidth
        if (mViewWidth > mViewHeight) {
            len = mViewHeight
        }
        centerCirclerRadius = (len / 8f)
        clipRegion = Region(-w, -h, w, h)
        // 准备中心圆的path
        centerCirclerPath.addCircle(0f, 0f, centerCirclerRadius, Path.Direction.CCW)
        centerRegion.setPath(centerCirclerPath, clipRegion)

        //计算画布坐标移动后的矩阵
        val tempMatrix = Matrix()
        tempMatrix.preTranslate(mViewWidth / 2f, mViewHeight / 2f)
        tempMatrix.preScale(1f, -1f)//旋转y轴
        //反转该矩阵，用来将点击的电换算成画布坐标系中的点
        tempMatrix.invert(canvasMatrix)
        println("画布矩阵" + canvasMatrix.toString())

        prepareRectF()

    }


    private fun prepareRectF() {
        //计算大扇形的半径
        bigArcRadius = (centerCirclerRadius) + arcHight + interval
        //小扇形的半径
        smallArcRadius = bigArcRadius - arcHight

        //大扇形的矩形
        bigrectF = RectF()
        bigrectF.set(-bigArcRadius, -bigArcRadius, bigArcRadius, bigArcRadius)
        //小扇形的矩形
        smallrectF = RectF()
        smallrectF.set(-smallArcRadius, -smallArcRadius, smallArcRadius, smallArcRadius)
    }


    private fun dp2px(dp: Float): Float {
        return DimenUtils.let {
            it.getContext(mCurrentContext)
            it.getDpMapPx(dp.toInt())
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPoint[0] = event.getX()
                touchPoint[1] = event.getY()
                println("转换之前${Arrays.toString(touchPoint)}")
                //反矩阵
                canvasMatrix.mapPoints(touchPoint)
                println("转换之后${Arrays.toString(touchPoint)}")
                invalidate()
                if (centerRegion.contains(touchPoint[0].toInt(), touchPoint[1].toInt())) {
                    println("点击了圆心")
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                touchPoint[0] = -1f
                touchPoint[1] = -1f
            }
            else -> {
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas?) {

        //画辅助坐标轴
        if (canvas != null) {
            canvas.translate(mViewWidth / 2f, mViewHeight / 2f)
            canvas.scale(1f, -1f)//旋转y轴
            //画中间的圆
            drawCenterCircle(canvas)
            //画边缘的四个扇形
            drawEdgeArc(canvas)
            //画辅助坐标
            drawAxis(canvas)

            mDefaultPaint.setColor(Color.RED)
            canvas.drawCircle(touchPoint[0], touchPoint[1], 8f, mDefaultPaint)

        }
    }

    private fun pathAddArc(
        path: Path,
        bigStartAngle: Float,
        sweepAngle: Float,
        canvas: Canvas,
        apply: () -> Unit
    ) {
        path.addArc(bigrectF, bigStartAngle, sweepAngle)
        path.arcTo(smallrectF, bigStartAngle + sweepAngle, -sweepAngle)
        path.close()
        apply()
        canvas.drawPath(path, mDefaultPaint)
    }

    private fun drawEdgeArc(canvas: Canvas) {
        mDefaultPaint.style = Paint.Style.FILL
        mDefaultPaint.setColor(backColor)
        //准备画上边扇形
        prepareTopArc(canvas)
        //准备画下边扇形
        prepareBottomArc(canvas)
        //画右边的扇形
        prepareRightArc(canvas)
        prepareLeftArc(canvas)
    }

    private fun prepareTopArc(canvas: Canvas) {
        pathAddArc(topArcPath, 40f, 100f, canvas) {

        }
    }

    private fun prepareBottomArc(canvas: Canvas) {
        pathAddArc(bottomArcPath, 220f, 100f, canvas) {

        }

    }

    private fun prepareRightArc(canvas: Canvas) {
        pathAddArc(rightArcPath, 325f, 70f, canvas) {

        }
    }

    private fun prepareLeftArc(canvas: Canvas) {
        pathAddArc(leftArcPath, 505f, 70f, canvas) {

        }
    }

    private fun drawCenterCircle(canvas: Canvas) {
        mDefaultPaint.style = Paint.Style.FILL
        canvas.drawPath(centerCirclerPath, mDefaultPaint)
//        canvas.drawRect(0f, 0f, traceRadius, traceRadius, mDefaultPaint)
    }

    private fun drawAxis(canvas: Canvas?) {
        // 设置轴长度
        CanvasAidUtils.set2DAxisLength(mViewWidth / 2 * 0.8f, mViewHeight / 2 * 0.8f)
        CanvasAidUtils.setDrawAid(isShowAxis)
        CanvasAidUtils.draw2DCoordinateSpace(canvas)
    }

}