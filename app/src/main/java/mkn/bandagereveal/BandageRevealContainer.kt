package mkn.bandagereveal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Outline
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.core.graphics.withTranslation
import kotlin.math.min

@SuppressLint("ViewConstructor", "ClickableViewAccessibility")
class BandageRevealContainer(context: Context, behindView: CardBehindView?, contentView: View?) :
    FrameLayout(context) {
    private var bitmap: Bitmap? = null
    private val srcRect = Rect()
    private val destRect = RectF()
    private val destClipPath = Path()
    private val shadowGradient = GradientDrawable(
        GradientDrawable.Orientation.RIGHT_LEFT,
        intArrayOf(
            Color.argb(30, 0, 0, 0),
            Color.TRANSPARENT,
        )
    )

    private val shadowClipPath = Path()
    private val shadowWidth = 16.dp

    private val clipRect = Rect()
    private val cardContainer = CardView(context) {
        cardElevation = 3.dpf
        radius = CARD_RADIUS
    }

    init {
        clipToPadding = false
        setPadding(14.dp, 2.dp, 14.dp, 2.dp)

        if (behindView != null) {
            addView(behindView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            behindView.clipToOutline = true
            behindView.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline) {
                    outline.setRoundRect(
                        3.dp,
                        3.dp,
                        behindView.measuredWidth - 3.dp,
                        behindView.measuredHeight - 3.dp,
                        CARD_RADIUS
                    )
                }
            }
        }

        addView(cardContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        if (contentView != null) {
            cardContainer.addView(
                contentView,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )
        }

        cardContainer.setOnTouchListener(object : OnTouchListener {
            private var startPoint = -1f
            private var scrolledAmount = -1f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val contentWidth = cardContainer.width
                val contentHeight = cardContainer.height

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startPoint = event.rawX
                        val source = createBitmapFromView(cardContainer)

                        val c = Canvas(source)

                        destClipPath.reset()
                        destClipPath.addRoundRect(
                            0f,
                            0f,
                            contentWidth.toFloat(),
                            contentHeight.toFloat(),
                            floatArrayOf(
                                0f,
                                0f,
                                CARD_RADIUS,
                                CARD_RADIUS,
                                CARD_RADIUS,
                                CARD_RADIUS,
                                0f,
                                0f,
                            ),
                            Path.Direction.CW
                        )
                        c.clipPath(destClipPath)

                        cardContainer.draw(c)

                        bitmap = reverseBitmap(source, contentWidth, contentHeight)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (event.rawX < startPoint) {
                            scrolledAmount = startPoint - event.rawX
                            val fraction = scrolledAmount / contentWidth
                            behindView?.setOffsetFraction(fraction)
                            srcRect.set(
                                0,
                                0,
                                scrolledAmount.toInt(),
                                contentHeight,
                            )

                            destRect.set(
                                contentWidth - scrolledAmount.times(2) + 14.dpf,
                                (-2).dpf,
                                contentWidth.toFloat() - scrolledAmount + 14.dpf,
                                contentHeight.toFloat() + 2.dpf
                            )

                            shadowGradient.setBounds(
                                if (destRect.width() < min(shadowWidth, scrolledAmount.toInt())) {
                                    destRect.left.toInt()
                                } else {
                                    destRect.right.toInt() - min(
                                        shadowWidth,
                                        scrolledAmount.toInt()
                                    )
                                },
                                destRect.top.toInt(),
                                destRect.right.toInt(),
                                destRect.bottom.toInt(),
                            )

                            clipCard(scrolledAmount)
                            invalidate()
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        bitmap = null
                        clipCard(0f)
                        invalidate()
                    }
                }

                return true
            }
        })
    }

    /**
     * @param scrolledAmount    To clip the card from right as much as user scrolled. Zero means
     * clear the clip bounds
     * */
    private fun clipCard(scrolledAmount: Float) {
        if (scrolledAmount == 0f) {
            cardContainer.clipBounds = null
            return
        }

        clipRect.set(
            0, 0, (cardContainer.width - scrolledAmount).toInt(), cardContainer.height
        )

        cardContainer.clipBounds = clipRect
    }

    private fun createBitmapFromView(contentView: View) : Bitmap {
        val width = contentView.width
        val height = contentView.height

        return Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    }

    private fun clipSourceBitmap(source: Bitmap): Canvas {
        val c = Canvas(source)
        destClipPath.apply {
            reset()
            addRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                floatArrayOf(
                    0f,
                    0f,
                    CARD_RADIUS,
                    CARD_RADIUS,
                    CARD_RADIUS,
                    CARD_RADIUS,
                    0f,
                    0f,
                ),
                Path.Direction.CW
            )
        }

        c.clipPath(destClipPath)
        return c
    }


    private fun reverseBitmap(source: Bitmap, w: Int, h: Int): Bitmap {
        val matrix = Matrix().apply {
            postScale(
                -1f,
                1f,
                w / 2f,
                h / 2f
            )
        }

        return Bitmap.createBitmap(
            source,
            0,
            0,
            w,
            h,
            matrix,
            true
        )
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        bitmap?.let { source ->
            canvas.withTranslation(y = 2.dpf) {
                drawBitmap(source, srcRect, destRect, null)
                clipShadow(canvas)

                val shadowAlpha = if (destRect.width() < shadowWidth) {
                    0f
                } else {
                    ((destRect.width() - shadowWidth) / shadowWidth.toFloat()).coerceAtMost(1f)
                }
                shadowGradient.alpha = (shadowAlpha * 255).toInt()
                shadowGradient.draw(canvas)

            }
        }
    }

    private fun clipShadow(canvas: Canvas) {
        shadowClipPath.reset()
        shadowClipPath.addRoundRect(
            destRect,
            floatArrayOf(
                CARD_RADIUS,
                CARD_RADIUS,
                0f,
                0f,
                0f,
                0f,
                CARD_RADIUS,
                CARD_RADIUS,
            ),
            Path.Direction.CW
        )
        canvas.clipPath(shadowClipPath)
    }
}

fun View.BandageRevealContainer(
    behindView: () -> CardBehindView? = { null },
    content: () -> View? = { null }
): BandageRevealContainer {
    return BandageRevealContainer(context, behindView(), content())
}