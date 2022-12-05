package com.example.assignment.ui.viewmodels

import androidx.lifecycle.*
import com.example.assignment.data.api.model.AuthData
import com.example.assignment.data.DataRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val user = MutableLiveData<AuthData>(null)

    val loading = MutableLiveData(false)

    fun login(name: String, password: String) {
        viewModelScope.launch {
            loading.postValue(true)
            repository.login(
                name,
                password,
                { _message.postValue(it) },
                { user.postValue(it) }
            )
            loading.postValue(false)
        }
    }

    fun signup(name: String, password: String){
        viewModelScope.launch {
            loading.postValue(true)
            repository.register(
                name,password,
                { _message.postValue(it) },
                { user.postValue(it) }
            )
            loading.postValue(false)
        }
    }
}