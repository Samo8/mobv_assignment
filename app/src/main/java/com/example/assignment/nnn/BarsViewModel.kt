package com.example.assignment.nnn

import androidx.lifecycle.*
import com.example.assignment.room.model.PubRoom
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
            repository.apiBarList { _message.postValue(Evento(it)) }
            loading.postValue(false)
            emitSource(repository.dbPubs())
        }

    fun refreshData(){
        viewModelScope.launch {
            loading.postValue(true)
            repository.apiBarList { _message.postValue(Evento(it)) }
            loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(Evento(msg))}
}