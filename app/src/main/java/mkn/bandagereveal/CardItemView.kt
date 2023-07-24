package mkn.bandagereveal

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes

class CardItemView(context: Context): LinearLayout(context) {
    private val startColor = Color.parseColor("#FFC5AB8B")
    private val endColor = Color.parseColor("#FFF2DECC")
    private val backgroundGradient = GradientDrawable(
        GradientDrawable.Orientation.LEFT_RIGHT,
        intArrayOf(startColor, endColor)
    )

    private val ivLogo: ImageView
    private val tvTitle: TextView
    private val ivChevron: ImageView

    init {
        background = backgroundGradient

        setPadding(12.dp, 0, 12.dp, 0)
        gravity = Gravity.CENTER_VERTICAL

        ivLogo = ImageView(context) {
            setPadding(10.dp, 10.dp, 10.dp, 10.dp)
            setColorFilter(endColor)
            background = MaterialShapeDrawable {
                fillColor = ColorStateList.valueOf(Color.WHITE)
                setCornerSize(16.dpf)
            }
        }

        tvTitle = TextView(context) {
            setTextColor(Color.WHITE)
        }

        ivChevron = ImageView(context) {
            setImageResource(R.drawable.chevron)
            setColorFilter(Color.BLACK)
        }

        addView(ivLogo, LayoutParams(42.dp, 42.dp))
        addView(tvTitle, LayoutParams(0, LayoutParams.WRAP_CONTENT){
            leftMargin = 12.dp
            rightMargin = 12.dp
            weight = 1f
        })

        addView(ivChevron, LayoutParams(16.dp, 16.dp))

    }

    fun bind(title: String, @DrawableRes icon: Int) {
        ivLogo.setImageResource(icon)
        tvTitle.text = title
    }
}

fun View.CardItemView(title: String, @DrawableRes icon: Int): CardItemView {
    return CardItemView(context).apply {
        bind(title, icon)
    }
}