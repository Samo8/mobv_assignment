package com.example.assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.assignment.common.Tags
import com.example.assignment.databinding.FragmentAnimationBinding
import com.example.assignment.databinding.FragmentBarDetailBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAGS = "tags"
private const val REMOVE_ITEM = "removeItem"

class BarDetailFragment : Fragment() {
    private var tags: Tags? = null
    private var removeItemHelper: RemoveItemHelper? = null

    private var _binding: FragmentBarDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tags = it.getParcelable(TAGS)
            removeItemHelper = it.getParcelable(REMOVE_ITEM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val floatingButton: FloatingActionButton = binding.floatingActionButtonRemoveBar
        floatingButton.setOnClickListener {
            if (removeItemHelper != null) {
                removeItemHelper!!.remove()
                findNavController().popBackStack()
            }
        }

        binding.tvBarName.text = tags?.name
        binding.tvStreet.text = tags?.addrStreet
        binding.tvCapacity.text = tags?.capacity
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BarDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(TAGS, param1)
                }
            }
    }
}