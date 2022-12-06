package com.example.assignment.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.DataRepository
import com.example.assignment.data.api.model.PubDetail
import kotlinx.coroutines.launch

class PubDetailViewModel(
    private val repository: DataRepository,
): ViewModel() {
    val user = MutableLiveData<PubDetail?>(null)

    val loading = MutableLiveData(false)

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun fetchPubDetail(pubId: String) {
        viewModelScope.launch {
            loading.postValue(true)
            repository.fetchPubDetail(
                pubId,
                { _message.postValue(it) },
                { user.postValue(it) }
            )
            loading.postValue(false)
        }
    }
}