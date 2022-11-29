@file:Suppress("DEPRECATION")

package com.example.topviewap.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.squareup.picasso.Transformation
import android.renderscript.Element

/**
 * 对图片进行高斯模糊处理
 */
class BlurTransformation(context: Context?) : Transformation {
    private var rs: RenderScript

    init {
        rs = RenderScript.create(context)
    }

    @SuppressLint("NewApi")
    override fun transform(bitmap: Bitmap): Bitmap {
        // 创建一个Bitmap作为最后处理的效果Bitmap
        val blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // 分配内存
        val input: Allocation = Allocation.createFromBitmap(
            rs,
            blurredBitmap,
            Allocation.MipmapControl.MIPMAP_FULL,
            Allocation.USAGE_SHARED
        )
        val output: Allocation = Allocation.createTyped(rs, input.type)

        // 根据我们想使用的配置加载一个实例
        val script: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setInput(input)

        // 设置模糊半径
        script.setRadius(10f)

        //开始操作
        script.forEach(output)

        // 将结果copy到blurredBitmap中
        output.copyTo(blurredBitmap)

        //释放资源
        bitmap.recycle()
        return blurredBitmap
    }

    override fun key(): String {
        return "blur"
    }
}
