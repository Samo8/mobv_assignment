package com.example.assignment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class RemoveItemHelper(
    val removeItem: (Int) -> Unit,
    val position: Int,
): Parcelable {
    fun remove() {
        removeItem(position)
    }
}