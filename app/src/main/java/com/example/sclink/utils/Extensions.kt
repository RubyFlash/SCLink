package com.example.sclink.utils

import android.content.BroadcastReceiver
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.Patterns
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URL
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun String.makeTextLink(clickableSpan: ClickableSpan): SpannableString? {
    try {
        val spannableString = SpannableString(this)
        val matcher = Patterns.WEB_URL.matcher(this)
        val matchStart: Int
        val matchEnd: Int

        while (matcher.find()) {
            matchStart = matcher.start(1)
            matchEnd = matcher.end()

            spannableString.setSpan(
                clickableSpan, matchStart, matchEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return spannableString
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun String.isValidUrl(): Boolean {
    try {
        return URLUtil.isValidUrl(this) && Patterns.WEB_URL.matcher(this).matches()
    } catch (exception: MalformedURLException) { }
    return false
}

fun RecyclerView.attachFab(fab: FloatingActionButton, action: (Boolean) -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(-1)) fab.show()
            else if (dy > 0 && fab.isShown) {
                action(true)
                fab.hide()
            }
            else if (dy < 0 && !fab.isShown) fab.show()
        }
    })
}