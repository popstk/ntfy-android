package io.heckel.ntfy.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceGroupAdapter
import androidx.recyclerview.widget.RecyclerView
import io.heckel.ntfy.R

class PreferenceGroupCardDecoration(
    private val horizontalMargin: Int,
    private val groupBottomMargin: Int,
    private val dividerColor: Int,
    private val dividerHeight: Float
) : RecyclerView.ItemDecoration() {

    private val dividerPaint = Paint().apply {
        color = dividerColor
        style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter as? PreferenceGroupAdapter ?: return
        val pos = parent.getChildAdapterPosition(view)
        if (pos < 0 || pos >= adapter.itemCount) return
        val item = adapter.getItem(pos)

        if (item is PreferenceCategory) return

        outRect.left = horizontalMargin
        outRect.right = horizontalMargin

        val isFirst = pos == 0 || adapter.getItem(pos - 1) is PreferenceCategory
        val isLast = pos == adapter.itemCount - 1 || adapter.getItem(pos + 1) is PreferenceCategory

        if (isLast) outRect.bottom = groupBottomMargin

        val bgRes = when {
            isFirst && isLast -> R.drawable.bg_pref_single
            isFirst -> R.drawable.bg_pref_top
            isLast -> R.drawable.bg_pref_bottom
            else -> R.drawable.bg_pref_middle
        }
        view.setBackgroundResource(bgRes)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter as? PreferenceGroupAdapter ?: return
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child)
            if (pos < 0 || pos >= adapter.itemCount) continue
            if (adapter.getItem(pos) is PreferenceCategory) continue
            val isLast = pos == adapter.itemCount - 1 || adapter.getItem(pos + 1) is PreferenceCategory
            if (isLast) continue
            val left = child.left.toFloat() + horizontalMargin
            val right = child.right.toFloat() - horizontalMargin
            val y = child.bottom.toFloat()
            c.drawRect(left, y, right, y + dividerHeight, dividerPaint)
        }
    }
}
