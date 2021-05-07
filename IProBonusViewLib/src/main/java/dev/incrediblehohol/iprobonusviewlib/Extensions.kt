package dev.incrediblehohol.iprobonusviewlib

import android.content.res.Resources
import android.util.TypedValue
import java.util.*

inline fun <T> T?.orIfNull(onNull: () -> T): T = this ?: onNull.invoke()

fun Date?.orNow() = this.orIfNull { Date() }

val Float.dpToPx: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this,
        Resources.getSystem().displayMetrics
    )