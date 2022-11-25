package com.example.assignment

import androidx.lifecycle.ViewModel

class PubsAroundViewModel: ViewModel() {
    private var _pubsAround: List<PubAround> = listOf()
    private var _selectedPubId: Long = 0

    val pubsAround: List<PubAround>
        get() = _pubsAround

    val selectedPubId: Long
        get() = _selectedPubId

    fun updatePubsAround(pubs: List<PubAround>) {
        _pubsAround = pubs
        _selectedPubId = if(pubs.isNotEmpty()) pubs.first().element.id else 0
    }

    fun updateIsSelected(pubId: Long) {
        _selectedPubId = pubId
    }

    fun getSelectedPub(): PubAround {
        return _pubsAround.first{ _selectedPubId == it.element.id }
    }
}