package com.example.assignment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.assignment.databinding.FragmentPubsAroundBinding
import com.example.assignment.pub_detail.Server
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.function.Consumer
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class PubsAroundFragment : Fragment() {
    private var _binding: FragmentPubsAroundBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val pubsAroundViewModel: PubsAroundViewModel by activityViewModels()

    private lateinit var pubsAroundListAdapter: PubsAroundAdapter
    private lateinit var recyclerViewPubsAround: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        pubsAroundListAdapter = PubsAroundAdapter(pubsAroundViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPubsAroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewPubsAround = binding.recyclerViewPubsAround
        recyclerViewPubsAround.layoutManager = LinearLayoutManager(context)
        recyclerViewPubsAround.adapter = pubsAroundListAdapter

        val progressBar: ProgressBar = binding.progressBarPubsAround
        val animationView: LottieAnimationView = binding.animationView

        animationView.setOnClickListener { animationView.playAnimation() }

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

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
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
                    }
                }
            }
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
    }
}