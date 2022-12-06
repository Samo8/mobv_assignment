package com.example.assignment.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.common.Evento
import com.example.assignment.data.DataRepository
import com.example.assignment.data.api.Friend
import kotlinx.coroutines.launch

class FriendListViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val friends = MutableLiveData<List<Friend>>(mutableListOf())

    val loading = MutableLiveData(false)

    fun fetchFriends() {
        viewModelScope.launch {
            loading.postValue(true)
            repository.fetchFriends(
                { _message.postValue(it) },
                { friends.postValue(it) }
            )
            loading.postValue(false)
        }
    }
}