package com.example.assignment.ui.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assignment.*
import com.example.assignment.databinding.FragmentBarsListBinding
import com.example.assignment.ui.viewmodels.BarsViewModel
import com.example.assignment.common.Injection
import com.example.assignment.data.database.model.PubRoom
import com.example.assignment.ui.adapters.BarsListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class BarsListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentBarsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewmodel: BarsViewModel

    private lateinit var barListAdapter: BarsListAdapter
    private lateinit var recyclerViewBarList: RecyclerView

    private lateinit var barListFragment: BarsListFragment
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                Toast.makeText(context, "${it.key} not granted", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        viewmodel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        )[BarsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        viewmodel.updateCurrentLocation(fusedLocationClient, requireContext())

        recyclerViewBarList = binding.recyclerViewBarsList
        recyclerViewBarList.layoutManager = LinearLayoutManager(context)

        progressBar = binding.progressBar
        val floatingActionButtonSort = binding.floatingActionButtonSort
        val spinner: Spinner = binding.spinner

        viewmodel.loading.observe(this.viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
            recyclerViewBarList.visibility = if (it) View.GONE else View.VISIBLE
        }

        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        val options = arrayOf(
            "Zvoľte zoradenie",
            "Názov",
            "Počet ľudí",
            "Vzdialenosť"
        )
        spinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, options)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                barListAdapter = BarsListAdapter(
                    sortPubs(
                        options[p2],
                        viewmodel.bars.value
                    ), barListFragment.findNavController())
                recyclerViewBarList.adapter = barListAdapter
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }


        swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        barListFragment = this

        viewmodel.bars.observe(this.viewLifecycleOwner) {
            barListAdapter = BarsListAdapter(
                it ?: mutableListOf(),
                barListFragment.findNavController()
            )
            recyclerViewBarList.adapter = barListAdapter
        }

        floatingActionButtonSort.setOnClickListener {
            findNavController().navigate(
                BarsListFragmentDirections.actionBarsListFragmentToAddFriendFragment()
            )
        }
    }

    private fun sortPubs(sortBy: String, pubs: List<PubRoom>?) : List<PubRoom> {
        if (pubs == null) {
            return  mutableListOf()
        }
        return when (sortBy) {
            "Názov" -> pubs.sortedBy { it.name }
            "Počet ľudí" -> pubs.sortedBy { it.users }
            "Vzdialenosť" ->
                if (viewmodel.currentLocation.value != null) {
                    println("LOCATION NOT NULL")
                    println(viewmodel.currentLocation.value)
                    return pubs.sortedBy { distanceInMeters(
                        it.lat.toDouble(),
                        it.lon.toDouble(),
                        viewmodel.currentLocation.value!!.latitude,
                        viewmodel.currentLocation.value!!.longitude
                    ) }
                } else {
                    return pubs.sortedBy { it.name }
                }

            else -> pubs
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRefresh() {
        viewmodel.refreshData()
        swipeRefresh.isRefreshing = false
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.app_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.friends -> {
                        findNavController().navigate(
                            BarsListFragmentDirections.actionBarsListFragmentToFriendsListFragment()
                        )
                        return true
                    }
                    R.id.location -> {
                        findNavController().navigate(
                            BarsListFragmentDirections.actionBarsListFragmentToPubsAroundFragment()
                        )
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
}