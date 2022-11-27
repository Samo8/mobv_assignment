package com.example.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.DataRepository
import com.example.assignment.data.api.JoinPubRequest
import com.example.assignment.ui.viewmodels.data.PubAround
import kotlinx.coroutines.launch

class PubsAroundViewModel(
    private val repository: DataRepository
): ViewModel() {
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

    fun joinPub(joinPubRequest: JoinPubRequest) {
        viewModelScope.launch {
            repository.joinPub(
                joinPubRequest,
                { },
                { }
            )
        }
    }
}