package com.example.assignment.ui.viewmodels

import android.location.Location
import androidx.lifecycle.*
import com.example.assignment.data.DataRepository
import com.example.assignment.data.api.JoinPubRequest
import com.example.assignment.ui.viewmodels.data.PubAround
import kotlinx.coroutines.launch

class PubsAroundViewModel(
    private val repository: DataRepository
): ViewModel() {
//    private var _pubsAround: List<PubAround> = listOf()
    val pubsAround = MutableLiveData<List<PubAround>>()

    private var _selectedPubId: Long = 0
    private val _message = MutableLiveData<String>()

    val loading = MutableLiveData(false)

//    val pubsAround: List<PubAround>
//        get() = _pubsAround
    val selectedPubId: Long
        get() = _selectedPubId
    val message: LiveData<String>
        get() = _message

    fun fetchPubsAround(location: Location) {
        viewModelScope.launch {
            println("BEFORE")
            loading.postValue(true)
            repository.fetchPubsAround(
                location,
                { _message.postValue(it) },
                { pubsAround.postValue(it) }
            )
            loading.postValue(false)
            println("AFTER")
        }
    }

    fun updatePubsAround(pubs: List<PubAround>) {
        pubsAround.postValue(pubs)
        _selectedPubId = if(pubs.isNotEmpty()) pubs.first().element.id else 0
    }

    fun updateIsSelected(pubId: Long) {
        _selectedPubId = pubId
    }

    fun getSelectedPub(): PubAround {
        return pubsAround.value!!.first{ _selectedPubId == it.element.id }
    }

    fun joinPub(joinPubRequest: JoinPubRequest) {
        viewModelScope.launch {
            repository.joinPub(
                request = joinPubRequest,
                onError = { _message.postValue(it) },
                onSuccess = { _message.postValue(it) },
                joining = true
            )
        }
    }
}