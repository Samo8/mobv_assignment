package com.example.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.nnn.DataRepository
import com.example.assignment.user.Friend
import kotlinx.coroutines.launch

class FriendListViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    val friends = MutableLiveData<List<Friend>>(mutableListOf())

    val loading = MutableLiveData(false)

    fun fetchFriends() {
        viewModelScope.launch {
            loading.postValue(true)
            repository.fetchFriends(
                { _message.postValue(Evento(it)) },
                { friends.postValue(it) }
            )
            loading.postValue(false)
        }
    }
}