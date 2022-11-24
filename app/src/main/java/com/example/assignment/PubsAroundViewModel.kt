package com.example.assignment

import androidx.lifecycle.ViewModel

class PubsAroundViewModel: ViewModel() {
    private var _pubsAround: List<PubAround> = listOf()
    private var _selectedPubINdex: Int = 0

    val pubsAround: List<PubAround>
        get() = _pubsAround

    val selectedPubIndex: Int
        get() = _selectedPubINdex

    fun updatePubsAround(pubs: List<PubAround>) {
        _pubsAround = pubs
        _selectedPubINdex = 0
    }

    fun updateIsSelected(pubIndex: Int) {
        _selectedPubINdex = pubIndex
    }
}