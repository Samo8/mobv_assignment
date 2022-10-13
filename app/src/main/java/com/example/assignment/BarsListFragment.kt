package com.example.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.common.BarData
import com.example.assignment.databinding.FragmentBarsListBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BarsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarsListFragment : Fragment() {
    private var _binding: FragmentBarsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBarsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewBarList: RecyclerView = binding.recyclerViewBarsList
        val progressBar: ProgressBar = binding.progressBar

        recyclerViewBarList.layoutManager = LinearLayoutManager(context)


        lateinit var jsonString: String
        try {
            jsonString = context?.assets?.open("pubs.json")
                ?.bufferedReader()
                .use { it?.readText() ?: "" }
            val barData = Json.decodeFromString<BarData>(jsonString)
            recyclerViewBarList.adapter = BarsListAdapter(barData, this)

            println(barData.elements.first().id)

        } catch (ioException: IOException) {
            println(ioException)
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