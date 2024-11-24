package com.example.assignment.utils

import android.content.Context
import android.view.View
import com.example.assignment.R
import kotlin.math.absoluteValue

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Context.toAmount(amount: Double): String {
    val string = if (amount > 0) R.string.lbl_pos_amt else R.string.lbl_neg_amt
    return this.getString(string, amount.absoluteValue)
}
