package com.example.assignment.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assignment.*
import com.example.assignment.databinding.FragmentBarsListBinding
import com.example.assignment.server.MpageServer
import com.example.assignment.ui.viewmodels.PubDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BarsListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentBarsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private val barDataViewModel: PubDataViewModel by activityViewModels()
    private lateinit var barListAdapter: BarsListAdapter
    private lateinit var recyclerViewBarList: RecyclerView

    private lateinit var barListFragment: BarsListFragment
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var notGranted = false
        permissions.entries.forEach {
            if (!it.value) {
                notGranted = true
                Toast.makeText(context, "${it.key} not granted", Toast.LENGTH_LONG).show()
            }
            Log.d("DEBUG", "${it.key} = ${it.value}")
        }
        if (!notGranted) {
            findNavController().navigate(
                BarsListFragmentDirections.actionBarsListFragmentToPubsAroundFragment()
            )
        }
    }

    private val pubsDBViewModel: PubsDBViewModel by activityViewModels {
        PubsViewModelFactory(
            (activity?.application as PubsApplication).database
                .pubDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(context)
        barListAdapter = BarsListAdapter(barDataViewModel, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        recyclerViewBarList = binding.recyclerViewBarsList
        recyclerViewBarList.layoutManager = LinearLayoutManager(context)

        recyclerViewBarList.adapter = barListAdapter

        progressBar = binding.progressBar
        val floatingActionButtonSort = binding.floatingActionButtonSort

        swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        barListFragment = this

        pubsDBViewModel.allItems.observe(this.viewLifecycleOwner) {
                items ->
                    when (items.isEmpty()) {
                        true -> {
                            fetchBarListFromApi()
                        }
                        else -> {
                            val pubs = items.map { it.toPubData() }

                            barDataViewModel.updatePubData(pubs)
                            barListAdapter = BarsListAdapter(barDataViewModel, barListFragment)
                            recyclerViewBarList.adapter = barListAdapter
                        }
                    }
        }

        floatingActionButtonSort.setOnClickListener {
            findNavController().navigate(
                BarsListFragmentDirections.actionBarsListFragmentToAddFriendFragment()
            )
        }

        if (recyclerViewBarList.adapter == null) {
            recyclerViewBarList.adapter = barListAdapter
        }

        progressBar.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchBarListFromApi() {
        CoroutineScope(Dispatchers.Main).launch {
            progressBar.visibility = View.VISIBLE
            recyclerViewBarList.visibility = View.INVISIBLE
            val data = MpageServer.fetchBarList(
                authData = sessionManager.fetchAuthData(),
                sessionManager = sessionManager,
            )

            pubsDBViewModel.deleteAll()
            barDataViewModel.updatePubData(data)
            pubsDBViewModel.addPubs(barDataViewModel.pubData)

            barListAdapter = BarsListAdapter(barDataViewModel, barListFragment)
            recyclerViewBarList.adapter = barListAdapter

            progressBar.visibility = View.INVISIBLE
            recyclerViewBarList.visibility = View.VISIBLE

        }
    }

    override fun onRefresh() {
        fetchBarListFromApi()
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
                        requestMultiplePermissions.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            )
                        )
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}