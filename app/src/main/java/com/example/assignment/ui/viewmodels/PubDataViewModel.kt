package com.example.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.assignment.common.PubData

class PubDataViewModel: ViewModel() {
    private var _pubData: List<PubData> = listOf()

    val pubData: List<PubData>
        get() = _pubData

    fun updatePubData(pubs: List<PubData>) {
        _pubData = pubs
    }
}