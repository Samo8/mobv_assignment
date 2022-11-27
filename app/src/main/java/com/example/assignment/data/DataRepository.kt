package com.example.assignment.data

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.assignment.auth.AuthData
import com.example.assignment.common.PubData
import com.example.assignment.data.api.*
import com.example.assignment.data.database.LocalCache
import com.example.assignment.pub_detail.model.PubDetail
import com.example.assignment.data.database.model.PubRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DataRepository private constructor(
    private val service: RestApi,
    private val cache: LocalCache,
){
    suspend fun register(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onStatus: (success: AuthData?) -> Unit
    ) {
        try {
            val resp = service.userCreate(UserSignRequest(name = name, password = password))
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
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val resp = service.addFriend(AddFriendRequest(friendName))

            if(resp.isSuccessful) {
               println("Added friend successfully")
            } else {
                println("Failed to add friend, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            println("Add friend failed, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Add friend failed, error.")
        }
    }

    suspend fun fetchFriends(
        onError: (error: String) -> Unit,
        onStatus: (success: List<Friend>?) -> Unit
    )= withContext(Dispatchers.IO) {
        try {
            val resp = service.fetchFriends()

            if (resp.isSuccessful) {
                resp.body()?.let { user ->
                    onStatus(user)
                }
            } else {
                onError("Failed to login, try again later.")
                onStatus(null)
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
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val resp = service.joinPub(request)

            if(resp.isSuccessful) {
                onSuccess("Joined pub successfully")
            } else {
                onError("Failed to join pub, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Join pub failed, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Join pub failed, error.")
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

    suspend fun fetchPubDetail(
        pubId: String,
        onError: (error: String) -> Unit,
        onStatus: (success: PubDetail?) -> Unit
    ) {
        try {
            val data = "[out:json];node(${pubId});out body;>;out skel;"
            val resp = service.fetchPubDetail(data)
            println(resp.raw().request().body())
            println(resp.raw().request().headers())
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

        fun getInstance(service: RestApi, cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(service, cache).also { INSTANCE = it }
            }

        @SuppressLint("SimpleDateFormat")
        fun dateToTimeStamp(date: String): Long {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)?.time ?: 0L
        }

        @SuppressLint("SimpleDateFormat")
        fun timestampToDate(time: Long): String{
            val netDate = Date(time*1000)
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(netDate)
        }
    }
}