package com.example.assignment

import androidx.lifecycle.*
import com.example.assignment.common.PubData
import com.example.assignment.room.dao.PubDao
import com.example.assignment.room.model.PubRoom
import kotlinx.coroutines.*
import okhttp3.Dispatcher

class PubsDBViewModel(
    private val pubDao: PubDao
): ViewModel() {
    val allItems: LiveData<List<PubRoom>> = pubDao.getAllPubs().asLiveData()

    private fun insertPub(pub: PubRoom) {
        viewModelScope.launch {
            pubDao.insert(pub)
        }
    }

    suspend fun addPubs(pubs: List<PubData>) {
        val roomPubs = pubs.map {
            PubRoom(
                id = it.bar_id,
                name = it.bar_name,
                lat = it.lat,
                lon = it.lon,
                type = it.bar_type,
                users = it.users,
                lastUpdate = it.last_update,
            )
        }
        pubDao.insertAll(roomPubs)
//        for (pub in pubs) {
//            addPubItem(
//                id = pub.bar_id, name = pub.bar_name, lat = pub.lat, lon = pub.lon,
//                type = pub.bar_type, users = pub.users, lastUpdate = pub.last_update
//            )
//        }
    }
    private fun addPubItem(
        id: String, name: String, lat: String, lon: String,
        type: String, users: String, lastUpdate: String
    ) {
        val pub = PubRoom(
            id = id,
            name = name,
            lat = lat,
            lon = lon,
            type = type,
            users = users,
            lastUpdate = lastUpdate,
        )
        insertPub(pub)
    }

    suspend fun deleteAll() {
        pubDao.deleteAll()
    }
}

class PubsViewModelFactory(private val pubDao: PubDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PubsDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PubsDBViewModel(pubDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}