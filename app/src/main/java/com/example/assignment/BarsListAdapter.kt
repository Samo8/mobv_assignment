package com.example.assignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.common.BarData


class BarsListAdapter(
    private val barData: BarData,
    private val barsListFragment: BarsListFragment,
) :
    RecyclerView.Adapter<BarsListAdapter.ViewHolder>() {
    private val elements = barData.elements

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        println("BAR DATA: $barData")
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.bars_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = elements[position].tags.name

        viewHolder.textView.setOnClickListener {
        barsListFragment.findNavController().navigate(R.id.action_barsListFragment_to_barDetailFragment)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = elements.size

}
