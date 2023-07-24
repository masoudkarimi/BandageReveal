package mkn.bandagereveal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ImageView.ScaleType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VerticalLayout(this) {
            setPadding(0, 12.dp, 0, 0)
            val itemsHeight = 72.dp

            addView(
                BandageRevealContainer(
                    behindView = {
                        CardBehindView(context)
                    },
                    content = {
                        CardItemView(
                            title = "View System",
                            icon = R.drawable.icon1
                        )
                    }
                ),
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemsHeight)
            )

            addView(
                BandageRevealContainer(
                    behindView = {
                        CardBehindView(context)
                    },
                    content = {
                        CardItemView(
                            title = "Masoud Karimi",
                            icon = R.drawable.puzzle
                        )
                    }
                ),
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemsHeight) {
                    topMargin = 12.dp
                }
            )

            addView(
                BandageRevealContainer(
                    behindView = {
                        CardBehindView(context)
                    },
                    content = {
                        ImageView(context) {
                            setImageResource(R.drawable.abstract_background)
                            scaleType = ScaleType.CENTER_CROP
                        }
                    }
                ),
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemsHeight) {
                    topMargin = 12.dp
                }
            )
        }.also { container ->
            setContentView(container)
        }

    }
}