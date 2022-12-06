package com.example.assignment.ui.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.example.assignment.data.DataRepository
import com.example.assignment.data.database.model.PubRoom
import com.example.assignment.common.Evento
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.launch

class BarsViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val loading = MutableLiveData(false)
    val currentLocation = MutableLiveData<Location>(null)

    var bars: LiveData<List<PubRoom>> =
        liveData {
            loading.postValue(true)
            repository.fetchPubs { _message.postValue(it) }
            loading.postValue(false)
            emitSource(repository.dbPubs())
        }

//    val sortedBooks: LiveData<List<PubRoom>> = Transformations
//        .map(bars) { books -> books }

    fun refreshData(){
        viewModelScope.launch {
            loading.postValue(true)
            repository.fetchPubs { _message.postValue(it) }
            loading.postValue(false)
        }
    }

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