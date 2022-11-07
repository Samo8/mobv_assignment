package com.example.assignment

import androidx.lifecycle.*
import com.example.assignment.common.Element
import com.example.assignment.common.ElementRoom
import com.example.assignment.common.TagsRoom
import com.example.assignment.dao.ElementDao
import kotlinx.coroutines.launch

class PubsDBViewModel(private val pubDao: ElementDao): ViewModel() {
    val allItems: LiveData<List<ElementRoom>> = pubDao.getAllPubs().asLiveData()

    private fun insertPub(pub: ElementRoom) {
        viewModelScope.launch {
            pubDao.insert(pub)
        }
    }

    private fun getNewPubEntry(id: Long, lat: Double, lon: Double, type: String, tags: TagsRoom): ElementRoom {
        return ElementRoom(
            id = id,
            lat = lat,
            lon = lon,
            type = type,
            tags = tags,
        )
    }

    suspend fun deletePub(element: ElementRoom) {
        pubDao.delete(element)
    }

    fun addPubs(pubs: List<Element>) {
        for (pub in pubs) {
            val tagsRoom = TagsRoom(
                bar = pub.tags.bar,
                email = pub.tags.email,
                name = pub.tags.name,
                url = pub.tags.url
            )
            addPubItem(id = pub.id, type = pub.type, lon = pub.lon, lat = pub.lat, tags = tagsRoom)
        }
    }

    fun addPubItem(id: Long, type: String, lat: Double, lon: Double, tags: TagsRoom) {
        val pub = getNewPubEntry(id, lat, lon, type, tags)
        insertPub(pub)
    }
}

class PubsViewModelFactory(private val pubDao: ElementDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PubsDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PubsDBViewModel(pubDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}