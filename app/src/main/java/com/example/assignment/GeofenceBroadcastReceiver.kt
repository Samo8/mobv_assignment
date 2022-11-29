package com.example.assignment

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.assignment.common.Injection
import com.example.assignment.data.DataRepository
import com.example.assignment.data.api.JoinPubRequest
import com.example.assignment.data.api.RestApi
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeofenceBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("ZAVOLANE")
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent!!.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            println("EXIT")
            val repository = Injection.provideDataRepository(
                context!!
            )
            CoroutineScope(Dispatchers.Main).launch {
                repository.joinPub(
                    request = JoinPubRequest(
                        id = "",
                        type = "",
                        lat = 0.0,
                        lon = 0.0,
                        name = ""
                    ),
                    onError = { println(it) },
                    onSuccess = { println(it) }
                )
            }
        }
    }
}