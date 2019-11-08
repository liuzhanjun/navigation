package com.hai.yun.cloudonfoot.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hai.yun.cloudonfoot.utils.DimenUtils
import com.hai.yun.cloudonfoot.utils.println
import androidx.core.view.ViewCompat.getMatrix
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*
import androidx.core.view.ViewCompat.getMatrix
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class PieView : View {
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    var datas: MutableList<PieData> = mutableListOf()
    private var paint: Paint = Paint()
    //单位是dp
    var interval: Float = 8f
    //单位是px
    var nameIntervale: Float = 0f

    //起始点的坐标
    private lateinit var startPoint: FloatArray

    //字体大小单位是dp
    var textSize: Float = 20f

    //圆的半径0
    private var radius: Float = 0f
    //圆的外接矩形
    private val cicleRectF = RectF()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initPaint()
        DimenUtils(context).let {
            interval = it.getDpMapPx(interval.toInt())
            textSize = it.getDpMapPx(textSize.toInt())
        }


    }

    private fun prepareDrawParam() {
        //计算圆的半径
        radius = if (mWidth > mHeight) mHeight / 2f else mWidth / 2f
        radius = radius - interval

        //圆的外接矩形
        cicleRectF.set(-radius, -radius, radius, radius)
        //初始化起始点的坐标
        startPoint = floatArrayOf(radius - (textSize) + nameIntervale, 0f)


    }

    private fun initPaint() {
        paint.setColor(Color.BLACK)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.setAntiAlias(true);
    }

    fun addData(datas_: MutableList<PieData>) {
        datas.clear()
        datas.addAll(datas_)
    }
    //不重写onMeasure，由父容器决定大小
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
////        1：获得此view上级容器为其推荐的宽和高，以及计算模式
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        val widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec);
////        3： 确定自己的高度和宽度
////        4： 设置自己的大小
//    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        //准备画图的一些元素
        prepareDrawParam()
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            //将坐标移动到中心
            canvas.translate(mWidth / 2f, mHeight / 2f)
            println("canvas======matrix" + canvas.matrix.toString())
            //画圆
            canvas.drawCircle(0f, 0f, radius, paint)
            //画主体
            drawPieBody(canvas)
            //画字体
            drawNames(canvas)
        }
    }


    //画主体
    private fun drawPieBody(canvas: Canvas) {
        var mEndAngle: Float = 0f
        run loop@{
            //遍历数据
            mEndAngle = datas.foldIndexed(0.0f) { index, startAngle, pieData ->
                //计算当前角度
                val angle = (360 * pieData.percentage)
                //计算结束角度
                val endAngle = (angle + startAngle).toFloat()
                //如果最终角度超过360度，则不画
                if (endAngle > 360) {
                    mEndAngle = startAngle
                    return@loop startAngle
                }
                //画扇形
                drawDataSrc(pieData, canvas, startAngle, angle)
                endAngle
            }

        }
        //小于360°那么剩下的画白色
        println("end===========${mEndAngle}")
        if (mEndAngle < 360) {
            paint.setColor(Color.parseColor("#F2F2F2"))
            canvas?.drawArc(cicleRectF, mEndAngle, 360 - mEndAngle, true, paint)
        }
    }

    private fun drawNames(canvas: Canvas) {
        var mEndAngle: Float = 0f
        run loop@{
            mEndAngle = datas.foldIndexed(0.0f) { index, startAngle, pieData ->
                paint.setColor(Color.BLACK)
                paint.textSize = textSize
                //计算当前角度
                val angle = (360 * pieData.percentage)
                //计算结束角度
                val endAngle = (angle + startAngle).toFloat()
                //如果最终角度超过360度，则不画
                if (endAngle > 360) {
                    mEndAngle = startAngle
                    return@loop startAngle
                }
//                //先确定第一个的位置
//                //通过矩阵转换位置
//                val mMatrix = Matrix()
//                mMatrix.postRotate(startAngle + (angle / 2))
//                val desc = FloatArray(2)
//                mMatrix.mapPoints(desc,startPoint)
//                canvas.drawText(pieData.name, desc[0], desc[1], paint)
                //字符的宽度
                val textLen = paint.measureText(pieData.name)

                println("==========text==${textLen}===")
                //保存操作之前的矩阵信息
                canvas.save()
                //以圆心为参照旋转到指定位置
                canvas.rotate(startAngle + (angle / 2))
                //以字符中心旋转90°
                canvas.rotate(90f, startPoint[0] + (textLen / 2), -(textSize / 2))
                canvas.drawText("${pieData.name}(${pieData.value})", startPoint[0], startPoint[1], paint)

                println("name======matrix" + canvas.matrix.toString())
                //重置矩阵信息
                canvas.restore()
                endAngle
            }
        }
        //小于360°怎么办
        if (mEndAngle < 360) {

            val info = "空白"
            val textLen = paint.measureText(info)
            //保存操作之前的矩阵信息
            canvas.save()
            //以圆心为参照旋转到指定位置
            canvas.rotate(mEndAngle + ((360 - mEndAngle) / 2))
            //以字符中心旋转90°
            canvas.rotate(90f, startPoint[0] + (textLen / 2), -(textSize / 2))
            canvas.drawText(info, startPoint[0], startPoint[1], paint)

            //重置矩阵信息
            canvas.restore()
        }
    }

    //画扇形
    private fun drawDataSrc(
        pieData: PieData,
        canvas: Canvas?,
        startAngle: Float,
        angle: Float
    ) {
        //设置颜色
        paint.setColor(pieData.color)
        paint.style = Paint.Style.FILL
        // 开始角度是startAngle
        canvas?.drawArc(cicleRectF, startAngle, angle, true, paint)
    }


    data class PieData(
        //名字
        var name: String,
        //值
        var value: Float,
        //百分比
        var percentage: Float,
        //颜色
        var color: Int = 0
    )
}