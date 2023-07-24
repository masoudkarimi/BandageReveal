package mkn.bandagereveal

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.updateLayoutParams

class CardBehindView(context: Context): FrameLayout(context) {

    private val ivDelete: ImageView

    init {
        setPadding(16.dp, 16.dp, 16.dp, 16.dp)
        setBackgroundColor(Color.parseColor("#F35950"))
        ivDelete = ImageView(context) {
            contentDescription = "Delete"
            setImageResource(R.drawable.stop)
            setColorFilter(Color.WHITE)
        }

        addView(ivDelete, LayoutParams(20.dp, 20.dp) {
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
        })
    }

    fun setOffsetFraction(x: Float) {
        ivDelete.updateLayoutParams<LayoutParams> {
            height = (20.dp + (6.dp * x)).toInt()
            width = (20.dp + (6.dp * x)).toInt()
            rightMargin = (x * 128.dpf).toInt()
        }
    }
}