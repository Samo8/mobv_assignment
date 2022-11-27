package com.example.assignment.ui.viewmodels

import androidx.lifecycle.*
import com.example.assignment.data.DataRepository
import com.example.assignment.data.database.model.PubRoom
import com.example.assignment.common.Evento
import kotlinx.coroutines.launch

class BarsViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    val loading = MutableLiveData(false)

    val bars: LiveData<List<PubRoom>> =
        liveData {
            loading.postValue(true)
            repository.fetchPubs { _message.postValue(Evento(it)) }
            loading.postValue(false)
            emitSource(repository.dbPubs())
        }

    fun refreshData(){
        viewModelScope.launch {
            loading.postValue(true)
            repository.fetchPubs { _message.postValue(Evento(it)) }
            loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(Evento(msg))}
}