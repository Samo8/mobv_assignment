package com.example.assignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.assignment.databinding.FragmentAnimationBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val NAME = "name"
private const val BAR_NAME = "barName"
private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"

/**
 * A simple [Fragment] subclass.
 * Use the [AnimationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnimationFragment : Fragment() {
    private var name: String? = null
    private var barName: String? = null
    private var latitude: Float? = null
    private var longitude: Float? = null

    private var _binding: FragmentAnimationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME)
            barName = it.getString(BAR_NAME)
            latitude = it.getFloat(LATITUDE)
            longitude = it.getFloat(LONGITUDE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewName: TextView = binding.textViewName
        val textViewBarName: TextView = binding.textViewBarName
        val buttonShowOnMap: Button = binding.buttonShowOnMap
        val animationView: LottieAnimationView = binding.animationView

        textViewName.text = name
        textViewBarName.text = barName

        animationView.setOnClickListener { animationView.playAnimation() }

        buttonShowOnMap.setOnClickListener {
            val mapUri: Uri = Uri.parse("geo:0,0?q=$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

//        animationView.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {}
//            override fun onAnimationEnd(animation: Animator) {}
//            override fun onAnimationCancel(animation: Animator) {}
//            override fun onAnimationRepeat(animation: Animator) {}
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimationBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnimationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnimationFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, param1)
                    putString(BAR_NAME, param2)
                }
            }
    }
}