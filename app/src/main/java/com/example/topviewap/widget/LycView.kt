package com.example.topviewap.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import com.example.music.MusicService
import com.example.topviewap.entries.Lyric

/**
 * 这个自定义view的功能就是滑动歌词
 * 这个view使用前记得把歌词集合set进来
 */
class LycView : View {
    private var lycList: ArrayList<Lyric>? = null

    //歌词颜色
    private var lrcTextColor = 0

    //当前歌词颜色
    private var highLineTextColor = 0
    private var mWidth = 0
    private var mHeight = 0

    //行间距
    private var lineSpacing = 0

    //字体大小
    private var textSize = 0

    private val lycPaint = Paint(Paint.ANTI_ALIAS_FLAG) //歌词画笔
    private val currentLycPaint = Paint(Paint.ANTI_ALIAS_FLAG) //当前歌词画笔

    //手势监听
    private var mGestureDetector: GestureDetector? = null


    constructor(context: Context?) : super(context) {
        init(context)
    }


    @SuppressLint("CustomViewStyleable")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, com.example.topviewap.R.styleable.LrcView)
        lrcTextColor =
            ta.getColor(com.example.topviewap.R.styleable.LrcView_lrcTextColor, Color.GRAY)
        highLineTextColor =
            ta.getColor(com.example.topviewap.R.styleable.LrcView_highLineTextColor, Color.WHITE)
        //获得屏幕密度
        val fontScale = context.resources.displayMetrics.scaledDensity;
        val scale = context.resources.displayMetrics.density;
        //默认字体大小为16sp
        textSize = ta.getDimensionPixelSize(
            com.example.topviewap.R.styleable.LrcView_textSize,
            (16 * fontScale).toInt()
        )
        //默认行间距为30dp
        lineSpacing = ta.getDimensionPixelSize(
            com.example.topviewap.R.styleable.LrcView_lineSpacing,
            (30 * scale).toInt()
        )
        //回收
        ta.recycle()
        init(context)
    }

    @SuppressLint("CustomViewStyleable")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    //初始化两只画笔
    private fun init(context: Context?) {
        this.mGestureDetector =
            GestureDetector(context, GestureListener())
        lycPaint.style = Paint.Style.FILL//填满
        lycPaint.isAntiAlias = true//抗锯齿
        lycPaint.color = lrcTextColor//画笔颜色
        lycPaint.textSize = textSize.toFloat()//歌词大小
        lycPaint.textAlign = Paint.Align.CENTER//文字居中

        currentLycPaint.style = Paint.Style.FILL//填满
        currentLycPaint.isAntiAlias = true//抗锯齿
        currentLycPaint.color = highLineTextColor//画笔颜色
        currentLycPaint.textSize = textSize.toFloat()//歌词大小
        currentLycPaint.textAlign = Paint.Align.CENTER//文字居中
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        getMeasuredWidthAndHeight();//得到测量后的宽高
        if (MusicService.mMediaPlayer.duration != 0 && lycList != null) {
            getCurrentPosition()//得到当前歌词的位置
            drawLrc(canvas!!)//画歌词
            scrollLrc()//歌词滑动
            postInvalidateDelayed(100)//延迟0.01s刷新
        }

    }

    private fun getMeasuredWidthAndHeight() {
        if (mWidth == 0 || mHeight == 0) {
            mWidth = measuredWidth
            mHeight = measuredHeight
        }
    }

    //记录当前歌词的位置
    private var currentPosition = 0

    //记录上一个位置
    private var lastPosition = 0

    //得到当前歌词的位置
    private fun getCurrentPosition() {
        //注意这里是直接获取的mediaPlayer的时间
        val curTime = MusicService.mMediaPlayer.currentPosition
        //如果当前的时间大于10分钟，证明歌曲未播放，则当前位置应该为0
        if (curTime < lycList!![0].startTime || curTime > 10 * 60 * 1000) {
            currentPosition = 0
            return
        } else if (curTime > lycList!![lycList!!.size - 1].startTime) {
            currentPosition = lycList!!.size - 1
            return
        }
        for (i in lycList!!.indices) {
            if (curTime >= lycList!![i].startTime && curTime <= lycList!![i + 1].startTime) {
                currentPosition = i
            }
        }
    }

    //画歌词
    private fun drawLrc(canvas: Canvas) {
        for (i in 0 until lycList!!.size) {
            if (currentPosition == i) { //如果是当前的歌词就用高亮的画笔画
                canvas.drawText(
                    lycList!![i].content,
                    (width / 2).toFloat(), (height / 2 + i * lineSpacing).toFloat(), currentLycPaint
                )
            } else {
                canvas.drawText(  //不是当前歌词普通画笔
                    lycList!![i].content,
                    (width / 2).toFloat(), (height / 2 + i * lineSpacing).toFloat(), lycPaint
                )
            }
        }
    }

    //歌词滑动
    private fun scrollLrc() {
        //下一句歌词的开始时间
        val startTime: Long = lycList!![currentPosition].startTime.toLong()
        val currentTime: Long = MusicService.mMediaPlayer.currentPosition.toLong()

        //判断是否换行,在0.5s内完成滑动，即实现弹性滑动
        val y =
            if (currentTime - startTime > 500) (currentPosition * lineSpacing).toFloat() else lastPosition * lineSpacing + (currentPosition - lastPosition) * lineSpacing * ((currentTime - startTime) / 500f)
        scrollTo(0, y.toInt())
        if (scrollY == currentPosition * lineSpacing) {
            lastPosition = currentPosition
        }
    }

    //手势监听器
    class GestureListener : SimpleOnGestureListener() {

        //按下：手指刚刚接触到触摸屏的那一刹那，就是触的那一下
        override fun onDown(e: MotionEvent?): Boolean {
            Log.e("zwyuu", "sdjjdksk")
            return true
        }

        //双击;在双击的第二下，Touch down时触发
        override fun onDoubleTap(e: MotionEvent): Boolean {
            Log.e("zwyuu", "sdj====")
            return super.onDoubleTap(e)
        }

        //滚动：手指在触摸屏上滑动
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.e("zwyuu", "1111111111sdjjdsfsfdksk")
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        //抛掷：手指在触摸屏上迅速移动，并松开的动作
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.e("zwyuu", "sdjj____(((((((((((((sk")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        //用来判定该次点击是SingleTap而不是DoubleTap
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.e("zwyuu", "sd8888888888888888")
            return super.onSingleTapConfirmed(e)
        }
    }


    /**
     * 自定义View控件引用到布局中时是先执行onDraw方法的
     * 所以正确是使用方法是lycView.setLycList().draw()
     */
    fun draw(): LycView {
        currentPosition = 0
        lastPosition = 0
        invalidate()
        return this
    }

    /**
     * 把解析过的歌词集合传给这个view
     */
    fun setLycList(lyc: ArrayList<Lyric>) {
        this.lycList = lyc
    }


}