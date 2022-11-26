package com.example.assignment.nnn

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.example.assignment.auth.AuthData
import com.example.assignment.room.model.PubRoom
import com.example.assignment.user.Friend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DataRepository private constructor(
    private val service: RestApi,
    private val cache: LocalCache
){
    suspend fun apiUserCreate(
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

    suspend fun apiUserLogin(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onStatus: (success: AuthData?) -> Unit
    ) {
        try {
            val resp = service.userLogin(UserSignRequest(name = name, password = password))
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

    suspend fun addFriend (
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

    suspend fun fetchFriends (
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

    suspend fun apiBarList(
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