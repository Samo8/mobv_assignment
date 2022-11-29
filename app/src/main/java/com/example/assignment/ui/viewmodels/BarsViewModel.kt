package com.example.assignment.ui.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.example.assignment.data.DataRepository
import com.example.assignment.data.database.model.PubRoom
import com.example.assignment.common.Evento
import com.example.assignment.pub_detail.Server
import com.example.assignment.ui.adapters.PubsAroundAdapter
import com.example.assignment.ui.viewmodels.data.PubAround
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarsViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    private val _loading = MutableLiveData(false)
    val loading
        get() = _loading
    val currentLocation = MutableLiveData<Location>(null)

    var bars: LiveData<List<PubRoom>> =
        liveData {
            _loading.postValue(true)
            repository.fetchPubs { _message.postValue(Evento(it)) }
            _loading.postValue(false)
            emitSource(repository.dbPubs())
        }

//    val sortedBooks: LiveData<List<PubRoom>> = Transformations
//        .map(bars) { books -> books }

    fun refreshData(){
        viewModelScope.launch {
            _loading.postValue(true)
            repository.fetchPubs { _message.postValue(Evento(it)) }
            _loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(Evento(msg))}

    fun updateCurrentLocation(
        fusedLocationClient: FusedLocationProviderClient,
        context: Context
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                currentLocation.postValue(location)
            }
    }
}