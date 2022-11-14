package com.example.assignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.PubsApplication
import com.example.assignment.PubsDBViewModel
import com.example.assignment.PubsViewModelFactory
import com.example.assignment.Server
import com.example.assignment.common.PubData
import com.example.assignment.databinding.FragmentBarsListBinding
import com.example.assignment.ui.viewmodels.PubDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BarsListFragment : Fragment() {
    private var _binding: FragmentBarsListBinding? = null
    private val binding get() = _binding!!

    private val barDataViewModel: PubDataViewModel by activityViewModels()
    private lateinit var barListAdapter: BarsListAdapter
    private lateinit var recyclerViewBarList: RecyclerView

    private val pubsDBViewModel: PubsDBViewModel by activityViewModels {
        PubsViewModelFactory(
            (activity?.application as PubsApplication).database
                .elementDao()
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

        barListAdapter = BarsListAdapter(barDataViewModel, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewBarList = binding.recyclerViewBarsList
        recyclerViewBarList.adapter = barListAdapter

        val progressBar: ProgressBar = binding.progressBar
        val floatingActionButtonSort = binding.floatingActionButtonSort

        val barListFragment = this

        pubsDBViewModel.allItems.observe(this.viewLifecycleOwner) {
                items ->
                    when (items.isEmpty()) {
                        true -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                val data = Server.post()

                                barDataViewModel.pubData = data
                                pubsDBViewModel.addPubs(barDataViewModel.pubData.elements)

                                barListAdapter = BarsListAdapter(barDataViewModel, barListFragment)
                                recyclerViewBarList.adapter = barListAdapter
                            }
                        }
                        else -> {
                            val data = items.toMutableList()

                            val elements = data.map { it.toElement() }

                            barDataViewModel.pubData = PubData(elements.toMutableList())
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

        recyclerViewBarList.layoutManager = LinearLayoutManager(context)
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