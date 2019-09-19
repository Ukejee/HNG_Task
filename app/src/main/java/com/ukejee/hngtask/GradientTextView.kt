package com.ukejee.hngtask

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 9/17/19
 */

class GradientTextView @JvmOverloads
constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0)
    : TextView(context, attributeSet, defStyle){



    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if(changed){

            paint.shader = createLinearGradient(width.toFloat()/2,
                0.toFloat(), width.toFloat()/2,
                height.toFloat() - (height * 0.25).toFloat(),
                ContextCompat.getColor(context, R.color.app_blue),
                ContextCompat.getColor(context, R.color.app_purple), Shader.TileMode.CLAMP)
        }
    }

    private fun createLinearGradient(x0: Float, y0: Float, x1: Float, y1:Float,
                       icolor0: Int, color1: Int, tile: Shader.TileMode): LinearGradient{
        return LinearGradient(x0, y0, x1, y1, icolor0, color1, tile)
    }


}