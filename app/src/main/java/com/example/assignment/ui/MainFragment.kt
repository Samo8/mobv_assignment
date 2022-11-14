package com.example.assignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.assignment.databinding.FragmentMainBinding

private const val NAME = "name"
private const val BAR_NAME = "barName"

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var editTextName: EditText
    private lateinit var editTextBarName: EditText
    private lateinit var editTextLatitude: EditText
    private lateinit var editTextLongitude: EditText

    private lateinit var confirmButton: Button

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextName = binding.editTextName
        editTextBarName = binding.editTextBarName
        editTextLatitude = binding.editTextLatitude
        editTextLongitude = binding.editTextLongitude
        confirmButton = binding.confirmButton

        confirmButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAnimationFragment(
                name = editTextName.text.toString(),
                barName = editTextBarName.text.toString(),
                latitude = editTextLatitude.text.toString().toFloat(),
                longitude = editTextLongitude.text.toString().toFloat()
            )
            findNavController().navigate(action)
//            findNavController().navigate(R.id.action_mainFragment_to_animationFragment,
//                bundleOf(
//                    "name" to editTextName.text.toString(),
//                    "barName" to editTextBarName.text.toString()
//                )
//            )
        }

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
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, param1)
                    putString(BAR_NAME, param2)
                }
            }
    }
}