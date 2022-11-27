package com.example.assignment.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.assignment.*
import com.example.assignment.databinding.FragmentPubsAroundBinding
import com.example.assignment.pub_detail.Server
import com.example.assignment.server.MpageServer
import com.example.assignment.ui.adapters.PubsAroundAdapter
import com.example.assignment.ui.viewmodels.PubsAroundViewModel
import com.example.assignment.ui.viewmodels.data.PubAround
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class PubsAroundFragment : Fragment() {
    private var _binding: FragmentPubsAroundBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient

    private val pubsAroundViewModel: PubsAroundViewModel by activityViewModels()

    private lateinit var pubsAroundListAdapter: PubsAroundAdapter
    private lateinit var recyclerViewPubsAround: RecyclerView
    private lateinit var animationView: LottieAnimationView


    private val requestBackgroundLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permission ->
        if (!permission) {
            Toast.makeText(context, "not granted", Toast.LENGTH_LONG).show()
        } else {
            joinPub(animationView)
        }
    }

    private fun joinPub(animationView: LottieAnimationView) {
        CoroutineScope(Dispatchers.Main).launch {
            val selectedPub = pubsAroundViewModel.getSelectedPub()

            try {
                MpageServer.joinPub(
                    PubsService.JoinPubRequest(
                        id = selectedPub.element.id.toString(),
                        type = selectedPub.element.type,
                        lat = selectedPub.element.lat,
                        lon = selectedPub.element.lon,
                        name = selectedPub.element.tags.name,
                    )
                )
                Toast.makeText(
                    context, "Úspešne pridaný do podniku: ${selectedPub.element.tags.name}",
                    Toast.LENGTH_LONG
                ).show()

                updateAnimationProgress(animationView, 75, 150)

                createFence(selectedPub.element.lat, selectedPub.element.lon)
            } catch (e: Exception) {
                println(e.toString())
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geofencingClient = LocationServices.getGeofencingClient(requireActivity())

        pubsAroundListAdapter = PubsAroundAdapter(pubsAroundViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubsAroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewPubsAround = binding.recyclerViewPubsAround
        recyclerViewPubsAround.layoutManager = LinearLayoutManager(context)
        recyclerViewPubsAround.adapter = pubsAroundListAdapter

        val progressBar: ProgressBar = binding.progressBarPubsAround
        val joinPubButton: Button = binding.buttonJoinPub
        animationView = binding.animationView

        joinPubButton.setOnClickListener {
            requestBackgroundLocationPermission.launch(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

        animationView.setOnClickListener {
            animationView.playAnimation()
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
                if (location == null)
                    println("ERROR")
                else {
                    println(location.toString())
                    val lat = location.latitude
                    val lon = location.longitude

                    CoroutineScope(Dispatchers.Main).launch {
                        val response = Server.fetchPubsAround(location)
                        println(response)

                        val pubs = response.map {
                            PubAround(
                                element = it,
                                distance = distanceInMeters(lat, lon, it.lat, it.lon),
                            )
                        }.sortedBy { it.distance }
                        pubsAroundViewModel.updatePubsAround(pubs)

                        pubsAroundListAdapter = PubsAroundAdapter(pubsAroundViewModel)
                        recyclerViewPubsAround.adapter = pubsAroundListAdapter

                        progressBar.visibility = View.GONE
                        updateAnimationProgress(animationView, 0, 75)
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun createFence(lat: Double, lon: Double) {
        val geofenceIntent = PendingIntent.getBroadcast(
            requireContext(), 0,
            Intent(requireContext(), GeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
//            PendingIntent.FLAG_MUTABLE
        )

        val request = GeofencingRequest.Builder().apply {
            addGeofence(
                Geofence.Builder()
                    .setRequestId("mygeofence")
                    .setCircularRegion(lat, lon, 300F)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )
        }.build()

        println(request.toString())

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        geofencingClient.addGeofences(request, geofenceIntent).run {
            addOnSuccessListener {
                Toast.makeText(context, "Geofence(s) added", Toast.LENGTH_LONG).show()
            }
            addOnFailureListener {
                Toast.makeText(context, "Failed to add geofence(s)", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        }
    }

    private fun updateAnimationProgress(animationView: LottieAnimationView, min: Int, max: Int) {
        animationView.setMinAndMaxFrame(min, max)
        animationView.playAnimation()
    }

    private fun distanceInMeters (lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344
        return dist * 1000
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        pubsAroundViewModel.updatePubsAround(listOf())
    }
}