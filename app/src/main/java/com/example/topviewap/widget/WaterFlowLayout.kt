package com.example.topviewap.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class WaterFlowLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ViewGroup(context, attrs, defStyleAttr) {
    /**
     * 内部list为每行的view集合
     */
    private val mAllViews: MutableList<MutableList<View>> = ArrayList()

    /**
     * 所有行高的集合
     */
    private val mLineHeights: MutableList<Int> = ArrayList()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val childCount = childCount
        //记录控件的总宽度和总高度
        var width = 0
        var height = 0
        //记录每一行的宽度和高度
        var lineWidth = 0
        var lineHeight = 0
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            //测量子View的宽高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            //得到layoutParams
            val lp = childView.layoutParams as MarginLayoutParams
            //获取子view的宽度
            val childWidth = childView.measuredWidth + lp.leftMargin + lp.rightMargin
            //获取子view的高度
            val childHeight = childView.measuredHeight + lp.topMargin + lp.bottomMargin
            if (lineWidth + childWidth > widthSize - paddingLeft - paddingRight) {
                //换行
                width = Math.max(width, lineWidth)
                //重置行宽开辟一个新行
                lineWidth = childWidth
                //记录行高
                height += lineHeight
                lineHeight = childHeight
            } else { //不换行
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }
            if (i == childCount - 1) {
                //对比最后一行的宽度与记录的宽度
                width = Math.max(width, lineWidth)
                //加上最后一行的高度
                height += lineHeight
            }
        }
        setMeasuredDimension(
            if (widthMode == MeasureSpec.AT_MOST) width + paddingLeft + paddingRight else widthSize,
            if (heightMode == MeasureSpec.AT_MOST) height + paddingTop + paddingBottom else heightSize
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAllViews.clear()
        mLineHeights.clear()

        //获取当前viewGroup的宽度
        val width = width
        var lineWidth = 0
        var lineHeight = 0
        var lineViews: MutableList<View> = ArrayList()
        val childCount = childCount
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val lp = childView.layoutParams as MarginLayoutParams
            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight
            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width - paddingLeft - paddingRight) {
                //记录行高
                mLineHeights.add(lineHeight)
                //记录该行所有view
                mAllViews.add(lineViews)
                //重置行宽和行高
                lineWidth = 0
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin
                //重置行的view集合
                lineViews = ArrayList()
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin) + 6
            lineViews.add(childView)
        }
        //处理最后一行
        mAllViews.add(lineViews)
        mLineHeights.add(lineHeight)

        //确定子view的位置
        var left = paddingLeft
        var top = paddingTop
        val lineNum = mAllViews.size
        for (i in 0 until lineNum) {
            //当前行所有的view
            lineViews = mAllViews[i]
            lineHeight = mLineHeights[i]
            for (j in lineViews.indices) {
                val child = lineViews[j]
                if (child.visibility == GONE) {
                    continue
                }
                val lp = child.layoutParams as MarginLayoutParams
                val lc = left + lp.leftMargin
                val tc = top + lp.topMargin
                val rc = lc + child.measuredWidth
                val bc = tc + child.measuredHeight
                //为子view布局
                child.layout(lc, tc, rc, bc)
                left += child.measuredWidth + lp.leftMargin + lp.rightMargin
            }
            //换行是left清0 top累加行高
            left = paddingLeft
            top += lineHeight
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}