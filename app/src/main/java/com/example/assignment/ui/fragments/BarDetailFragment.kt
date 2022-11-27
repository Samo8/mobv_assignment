package com.example.assignment.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.assignment.R
import com.example.assignment.databinding.FragmentBarDetailBinding
import com.example.assignment.common.Injection
import com.example.assignment.ui.viewmodels.PubDetailViewModel

private const val PUB_ID = "id"
private const val PEOPLE_PRESENT_COUNT = "peoplePresentCount"

class BarDetailFragment : Fragment() {
    private var pubId: String? = null
    private var peoplePresentCount: String? = null

    private var _binding: FragmentBarDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var pubDetailViewModel: PubDetailViewModel

    private var lat: Double? = null
    private var lon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            pubId = it.getString(PUB_ID)
            peoplePresentCount = it.getString(PEOPLE_PRESENT_COUNT)
        }

        pubDetailViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        )[PubDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewBarName = binding.tvBarName
        val textViewStreet = binding.tvStreet
        val textViewCapacity = binding.tvCapacity
        val textViewPeoplePresentCount = binding.textViewPeoplePresentCount
        val buttonShowOnMap = binding.buttonShowOnMap
        val progressBarPubDetail = binding.progressBarPubDetail

        textViewPeoplePresentCount.text = String.format(
            "%s: %s",
            getString(R.string.people_count),
            peoplePresentCount
        )
        buttonShowOnMap.visibility = View.INVISIBLE
        progressBarPubDetail.visibility = View.VISIBLE

        buttonShowOnMap.setOnClickListener {
            if (lat != null && lon != null) {
                val mapUri: Uri = Uri.parse("geo:10,0?q=${lat},${lon}")
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }

        pubDetailViewModel.fetchPubDetail(pubId!!, context)

        pubDetailViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                val pubDetail = it.elements.first()

                lat = pubDetail.lat
                lon = pubDetail.lon

                buttonShowOnMap.visibility = View.VISIBLE
                progressBarPubDetail.visibility = View.GONE

                textViewBarName.text = pubDetail.tags.name
                textViewStreet.text = pubDetail.tags.addrStreet
                textViewCapacity.text = pubDetail.tags.capacity
            } else {
                buttonShowOnMap.visibility = View.GONE
                progressBarPubDetail.visibility = View.VISIBLE
            }
        }



//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                val response = Server.fetchPubDetail(pubId!!)
//
//                val pubDetail = response.elements.first()
//
//                lat = pubDetail.lat
//                lon = pubDetail.lon
//
//                buttonShowOnMap.visibility = View.VISIBLE
//                progressBarPubDetail.visibility = View.GONE
//
//                textViewBarName.text = pubDetail.tags.name
//                textViewStreet.text = pubDetail.tags.addrStreet
//                textViewCapacity.text = pubDetail.tags.capacity
//            } catch (e: Exception) {
//                println(e.toString())
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        lat = null
        lon = null
    }
}