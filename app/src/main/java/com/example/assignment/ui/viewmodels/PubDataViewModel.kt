package com.example.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.assignment.common.PubData

class PubDataViewModel: ViewModel() {
    var _pubData: List<PubData> = mutableListOf()

    val pubData: List<PubData>
        get() = _pubData

    fun updatePubData(pubs: List<PubData>) {
        _pubData = pubs
    }
}