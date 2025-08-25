package com.dubu.common.ext

import android.content.Context
import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dubu.common.R
import java.io.Closeable

fun <E> Collection<E>?.isValid() = !isNullOrEmpty()
fun String?.isValid() = !isNullOrEmpty()

fun Closeable.closeQuietly() {
    try {
        close()
    } catch (e: Exception) {
    }
}

fun Int?.toBool() = this == 1
fun Int?.toOpBool() = this != 1

fun Boolean?.toInt() = if (this == true) 1 else 0

fun Context.getDimen(id: Int) = resources.getDimension(id).toInt()


fun Int.toUnread() = if (this <= 0) "0" else if (this < 100) this.toString() else "99+"
fun Int.toK(): String {
    val value = if (this < 0) 0 else this
    return if (value < 1000) value.toString()
    else if (value < 1000_000) {
        String.format("%.1fk", value / 1000f)
    } else {
        String.format("%.1fm", value / 1000_000f)
    }
}

fun Fragment.show(manager: FragmentManager) {
    manager.beginTransaction().add(this, this.javaClass.name).commitAllowingStateLoss()
}

fun Int?.female() = (this == 2)


fun RecyclerView.disableItemAnimation() {
    itemAnimator?.apply {
        if (this is SimpleItemAnimator) {
            supportsChangeAnimations = false
        }
        changeDuration = 0L
        addDuration = 0L
        moveDuration = 0L
        removeDuration = 0L
    }
}


fun Int.toKRound() = if (this < 1000) toString() else String.format("%.0fk", this / 1000f)




/**
 *  @author  Even
 *  @date   2021/11/25
 */
@BindingAdapter("isActive")
fun View.setIsActive(isActive: Boolean) {
    this.isActivated = isActive
}

fun View.showView(isShow:Boolean){
    visibility = if (isShow) View.VISIBLE else View.GONE
}
fun View.toggleView(){
    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
}
/**
 * 防止重复点击
 * 重复间隔
 * 事件响应
 */
fun View.clickNoRepeat(interval: Long = 400, onClick: (View) -> Unit) {
    setOnClickListener {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime < interval) return@setOnClickListener
        lastClickTime = now
        onClick(it)
    }
}

private var View.lastClickTime: Long
    get() = getTag(R.id.last_click_time) as? Long ?: 0
    set(value) = setTag(R.id.last_click_time, value)