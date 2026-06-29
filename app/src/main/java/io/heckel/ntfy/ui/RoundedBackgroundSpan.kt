package io.heckel.ntfy.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.text.style.ReplacementSpan
import kotlin.math.roundToInt

/**
 * 把一段文本画成圆角药丸：半透明同色底 + 实色字 + 横向内边距。
 * 药丸文字比标题小一号（textScale），并在原行高内垂直居中，看起来像徽标。
 *
 * 用于详情列表标题里的类型标记（完成/需处理/中断），对应通知样式方案 B 的彩色词块。
 * 通知栏不经过这里，因此通知栏仍是纯文字。
 */
class RoundedBackgroundSpan(
    private val textColor: Int,
    private val bgColor: Int,
    private val paddingH: Float,        // 横向内边距(px)
    private val radius: Float,          // 圆角(px)
    private val marginEnd: Float,       // 药丸右侧外间距(px)
    private val textScale: Float = 0.7f,  // 药丸文字相对标题的缩放
    private val paddingV: Float = 2f    // 药丸上下额外留白(px)
) : ReplacementSpan() {

    private fun scaledPaint(base: Paint): TextPaint {
        val tp = TextPaint(base)
        tp.textSize = base.textSize * textScale
        return tp
    }

    override fun getSize(
        base: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?
    ): Int {
        val tp = scaledPaint(base)
        val width = tp.measureText(text, start, end)
        return (width + paddingH * 2 + marginEnd).roundToInt()
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, base: Paint
    ) {
        val tp = scaledPaint(base)
        val width = tp.measureText(text, start, end)

        // 以原标题文字的垂直中线为基准，把缩小后的药丸文字居中
        val fmBase = base.fontMetrics
        val centerY = y + (fmBase.ascent + fmBase.descent) / 2f
        val fmPill = tp.fontMetrics
        val baseline = centerY - (fmPill.ascent + fmPill.descent) / 2f

        val rect = RectF(
            x,
            baseline + fmPill.ascent - paddingV,
            x + width + paddingH * 2,
            baseline + fmPill.descent + paddingV
        )
        tp.color = bgColor
        canvas.drawRoundRect(rect, radius, radius, tp)
        tp.color = textColor
        canvas.drawText(text, start, end, x + paddingH, baseline, tp)
    }
}
