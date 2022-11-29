package com.example.assignment.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.DataRepository
import kotlinx.coroutines.launch

class AddFriendViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun addFriend(friendName: String) {
        viewModelScope.launch {
            repository.addFriend(
                friendName = friendName,
                onError = { _message.postValue(it) },
                onSuccess = { _message.postValue(it) }
            )
        }
    }
}