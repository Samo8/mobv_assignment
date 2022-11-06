package com.example.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.common.TagsRoom
import com.example.assignment.databinding.FragmentBarsListBinding
import com.example.assignment.viewmodels.PubDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.IOException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        // Inflate the layout for this fragment
        _binding = FragmentBarsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        barListAdapter = BarsListAdapter(barDataViewModel, this)

        val barListFragment = this
        CoroutineScope(Dispatchers.Main).launch {
            val data = Server.post()
            barDataViewModel.pubData = data

            pubsDBViewModel.addPubs(barDataViewModel.pubData.elements)

//            val elem = barDataViewModel.pubData.elements[0]

//            val tagsRoom = TagsRoom(
//                bar = elem.tags.bar,
//                email = elem.tags.email,
//                name = elem.tags.name,
//                url = elem.tags.url
//            )
//            pubsDBViewModel.addPubItem(id = elem.id, type = elem.type, lon = elem.lon, lat = elem.lat, tags = tagsRoom)

            barListAdapter = BarsListAdapter(barDataViewModel, barListFragment)
            recyclerViewBarList.adapter = barListAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewBarList = binding.recyclerViewBarsList
        val progressBar: ProgressBar = binding.progressBar
        val floatingActionButtonSort = binding.floatingActionButtonSort

        floatingActionButtonSort.setOnClickListener {
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BarsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}