package mkn.bandagereveal

import android.content.res.Resources

val CARD_RADIUS = 20.dpf

val Int.dp: Int
    get() {
        return (Resources.getSystem().displayMetrics.density * this).toInt()
    }

val Int.dpf: Float
    get() {
        return Resources.getSystem().displayMetrics.density * this
    }