package com.example.assignment.data

import android.location.Location
import androidx.lifecycle.LiveData
import com.example.assignment.data.api.model.AuthData
import com.example.assignment.common.DistanceService
import com.example.assignment.data.api.*
import com.example.assignment.data.database.LocalCache
import com.example.assignment.data.api.model.PubDetail
import com.example.assignment.data.database.model.PubRoom
import com.example.assignment.ui.viewmodels.data.PubAround
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class DataRepository private constructor(
    private val service: RestApi,
    private val cache: LocalCache,
    private val distanceService: DistanceService,
){
    suspend fun register(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onStatus: (success: AuthData?) -> Unit
    ) {
        try {
            val resp = service.userCreate(
                UserSignRequest(name = name, password = password)
            )
            if (resp.isSuccessful) {
                resp.body()?.let { user ->
                    if (user.uid == "-1"){
                        onStatus(null)
                        onError("Name already exists. Choose another.")
                    }else {
                        onStatus(user)
                    }
                }
            } else {
                onError("Failed to sign up, try again later.")
                onStatus(null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Sign up failed, check internet connection")
            onStatus(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Sign up failed, error.")
            onStatus(null)
        }
    }

    suspend fun login(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onStatus: (success: AuthData?) -> Unit
    ) {
        try {
            val resp = service.userLogin(
                UserSignRequest(name = name, password = password)
            )
            if (resp.isSuccessful) {
                resp.body()?.let { user ->
                    if (user.uid == "-1"){
                        onStatus(null)
                        onError("Wrong name or password.")
                    } else {
                        onStatus(user)
                    }
                }
            } else {
                onError("Failed to login, try again later.")
                onStatus(null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Login failed, check internet connection")
            onStatus(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Login in failed, error.")
            onStatus(null)
        }
    }

    suspend fun addFriend(
        friendName: String,
        onError: (error: String) -> Unit,
        onSuccess: (success: String) -> Unit
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val resp = service.addFriend(AddFriendRequest(friendName))

            if(resp.isSuccessful) {
                onSuccess("Added friend successfully")
            } else {
                onError("Failed to add friend, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Add friend failed, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Add friend failed, error.")
        }
    }

    suspend fun fetchFriends(
        onError: (error: String) -> Unit,
        onStatus: (success: List<Friend>) -> Unit
    )= withContext(Dispatchers.IO) {
        try {
            val resp = service.fetchFriends()

            if (resp.isSuccessful) {
                resp.body()?.let { user ->
                    onStatus(user)
                }
            } else {
                onError("Failed to login, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to fetch friends")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to fetch friends")
        }
    }

    suspend fun joinPub(
        request: JoinPubRequest,
        onError: (error: String) -> Unit,
        onSuccess: (error: String) -> Unit,
        joining: Boolean,
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val resp = service.joinPub(request)

            if(resp.isSuccessful) {
                if (joining)
                    onSuccess("Joined pub successfully")
                else
                    onSuccess("Left pub successfully")
            } else {
                if (joining)
                    onError("Failed to join pub, try again later.")
                else
                    onError("Failed to left pub, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            if (joining)
                onError("Join pub failed, check internet connection")
            else
                onError("Left pub failed, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (joining)
                onError("Join pub failed, error.")
            else
                onError("Left pub failed, error.")
        }
    }

    suspend fun fetchPubs(
        onError: (error: String) -> Unit,
    ) {
        try {
            val resp = service.barList()
            if (resp.isSuccessful) {
                resp.body()?.let { bars ->
                    val b = bars.map {
                        PubRoom(
                            id = it.bar_id,
                            name = it.bar_name,
                            type = it.bar_type,
                            lat = it.lat,
                            lon = it.lon,
                            users = it.users,
                            lastUpdate = it.last_update
                        )
                    }
                    cache.deletePubs()
                    cache.addPubs(b)
                } ?: onError("Failed to load bars")
            } else {
                onError("Failed to read bars")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load bars, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load bars, error.")
        }
    }

    suspend fun fetchPubsAround(
        location: Location,
        onError: (error: String) -> Unit,
        onStatus: (success: List<PubAround>) -> Unit,
    ) {
        try {
            val data = "[out:json];node(around:250,${location.latitude}, ${location.longitude});(node(around:250)[\"amenity\"~\"^pub\$|^bar\$|^restaurant\$|^cafe\$|^fast_food\$|^stripclub\$|^nightclub\$\"];);out body;>;out skel;"
            val resp = service.fetchPubsAround(data)
            if (resp.isSuccessful) {
                resp.body()?.let { pubResponse ->
                    val elem = pubResponse.elements.map {
                        PubAround(
                            element = it,
                            distance = distanceService.distanceInMeters(location.latitude, location.longitude, it.lat, it.lon),
                        )
                    }.sortedBy { it.distance }
                    onStatus(elem)
                }
            } else {
                onError("Failed to fetch pub detail, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Pub detail failed, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Pub detail failed")
        }
    }

    suspend fun fetchPubDetail(
        pubId: String,
        onError: (error: String) -> Unit,
        onStatus: (success: PubDetail?) -> Unit
    ) {
        try {
            val data = "[out:json];node(${pubId});out body;>;out skel;"
            val resp = service.fetchPubDetail(data)
            if (resp.isSuccessful) {
                resp.body()?.let { pub ->
                    onStatus(pub)
                }
            } else {
                onError("Failed to fetch pub detail, try again later.")
                onStatus(null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Pub detail failed, check internet connection")
            onStatus(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Pub detail failed")
            onStatus(null)
        }
    }

    fun dbPubs() : LiveData<List<PubRoom>> {
        return cache.getPubs()
    }

    companion object{
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(service: RestApi, cache: LocalCache, distanceService: DistanceService): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(service, cache, distanceService).also { INSTANCE = it }
            }
    }
}