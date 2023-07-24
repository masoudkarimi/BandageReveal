package mkn.bandagereveal

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.shape.MaterialShapeDrawable

fun ImageView(context: Context, block: ImageView.() -> Unit): ImageView {
    return ImageView(context).apply(block)
}

fun TextView(context: Context, block: TextView.() -> Unit): TextView {
    return TextView(context).apply(block)
}

fun CardView(context: Context, block: CardView.() -> Unit): CardView {
    return CardView(context).apply(block)
}

fun MaterialShapeDrawable(block: MaterialShapeDrawable.() -> Unit) = MaterialShapeDrawable().apply(block)

fun VerticalLayout(context: Context, block: LinearLayout.() -> Unit) : LinearLayout = LinearLayout(context).apply {
    orientation = LinearLayout.VERTICAL
    block()
}

fun FrameLayout.LayoutParams(width: Int, height: Int, block: FrameLayout.LayoutParams.() -> Unit = {}) = FrameLayout.LayoutParams(width, height).apply(block)

fun LinearLayout.LayoutParams(width: Int, height: Int, block: LinearLayout.LayoutParams.() -> Unit = {}) = LinearLayout.LayoutParams(width, height).apply(block)