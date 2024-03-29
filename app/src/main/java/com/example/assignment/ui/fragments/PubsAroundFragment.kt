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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.assignment.*
import com.example.assignment.R
import com.example.assignment.common.Injection
import com.example.assignment.data.api.JoinPubRequest
import com.example.assignment.databinding.FragmentPubsAroundBinding
import com.example.assignment.ui.adapters.PubsAroundAdapter
import com.example.assignment.ui.viewmodels.PubsAroundViewModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

class PubsAroundFragment : Fragment() {
    private var _binding: FragmentPubsAroundBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient

    private lateinit var pubsAroundViewModel: PubsAroundViewModel

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
        val selectedPub = pubsAroundViewModel.getSelectedPub()

        if (selectedPub != null) {
            pubsAroundViewModel.joinPub(
                JoinPubRequest(
                    id = selectedPub.element.id.toString(),
                    type = selectedPub.element.type,
                    lat = selectedPub.element.lat,
                    lon = selectedPub.element.lon,
                    name = selectedPub.element.tags.name,
                )
            )

            updateAnimationProgress(animationView, 75, 150)
            createFence(selectedPub.element.lat, selectedPub.element.lon)
        } else {
            Toast.makeText(context, getString(R.string.no_pub_selected), Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pubsAroundViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        )[PubsAroundViewModel::class.java]

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geofencingClient = LocationServices.getGeofencingClient(requireActivity())
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

        val progressBar: ProgressBar = binding.progressBarPubsAround
        val joinPubButton: Button = binding.buttonJoinPub
        animationView = binding.animationView

        pubsAroundViewModel.message.observe(this.viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        joinPubButton.setOnClickListener {
            requestBackgroundLocationPermission.launch(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

        pubsAroundViewModel.loading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        pubsAroundViewModel.pubsAround.observe(viewLifecycleOwner) {
            pubsAroundListAdapter = PubsAroundAdapter(
                it,
                pubsAroundViewModel
            )
            recyclerViewPubsAround.adapter = pubsAroundListAdapter
            updateAnimationProgress(animationView, 0, 75)
        }

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

                    pubsAroundViewModel.fetchPubsAround(location)
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun createFence(lat: Double, lon: Double) {
        val geofenceIntent = PendingIntent.getBroadcast(
            requireContext(), 0,
            Intent(requireContext(), GeofenceBroadcastReceiver::class.java),
            FLAG_UPDATE_CURRENT
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
                Toast.makeText(context, "Geofence added", Toast.LENGTH_LONG).show()
            }
            addOnFailureListener {
                Toast.makeText(context, "Failed to add geofence", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        }
    }

    private fun updateAnimationProgress(animationView: LottieAnimationView, min: Int, max: Int) {
        animationView.setMinAndMaxFrame(min, max)
        animationView.playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}