package com.example.assignment.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.*
import com.example.assignment.databinding.FragmentBarsListBinding
import com.example.assignment.server.MpageServer
import com.example.assignment.ui.viewmodels.PubDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BarsListFragment : Fragment() {
    private var _binding: FragmentBarsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private val barDataViewModel: PubDataViewModel by activityViewModels()
    private lateinit var barListAdapter: BarsListAdapter
    private lateinit var recyclerViewBarList: RecyclerView

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

        recyclerViewBarList = binding.recyclerViewBarsList
        recyclerViewBarList.layoutManager = LinearLayoutManager(context)

        recyclerViewBarList.adapter = barListAdapter

        val progressBar: ProgressBar = binding.progressBar
        val floatingActionButtonSort = binding.floatingActionButtonSort


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

        val barListFragment = this

        pubsDBViewModel.allItems.observe(this.viewLifecycleOwner) {
                items ->
                    when (items.isEmpty()) {
                        true -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                val data = MpageServer.fetchBarList(
                                    authData = sessionManager.fetchAuthData(),
                                    sessionManager = sessionManager,
                                )

                                barDataViewModel.updatePubData(data)
                                pubsDBViewModel.addPubs(barDataViewModel.pubData)

                                barListAdapter = BarsListAdapter(barDataViewModel, barListFragment)
                                recyclerViewBarList.adapter = barListAdapter

                            }
                        }
                        else -> {
                            val data = items.toMutableList()

                            val pubs = data.map { it.toPubData() }

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

//            CoroutineScope(Dispatchers.Main).launch {
//                pubsDBViewModel.deletePub(pubsDBViewModel.allItems.value!![0])
//            }


//            barListAdapter = BarsListAdapter(
//                barDataViewModel.pubData!!.elements.sortedBy { it.tags.name }
//                    .filter { it.tags.name != null }
//                    .toMutableList(),
//                this
//            )
//            recyclerViewBarList.adapter = barListAdapter
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
}