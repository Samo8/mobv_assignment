package com.example.assignment.ui.viewmodels

import androidx.lifecycle.*
import com.example.assignment.auth.AuthData
import com.example.assignment.common.Evento
import com.example.assignment.data.DataRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    val user= MutableLiveData<AuthData>(null)

    val loading = MutableLiveData(false)

    fun login(name: String, password: String) {
        viewModelScope.launch {
            loading.postValue(true)
            repository.login(
                name,
                password,
                { _message.postValue(Evento(it)) },
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
                { _message.postValue(Evento(it)) },
                { user.postValue(it) }
            )
            loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(Evento(msg))}
}