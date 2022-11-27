package com.example.assignment.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.data.database.model.PubRoom
import com.example.assignment.ui.fragments.BarsListFragment
import com.example.assignment.ui.fragments.BarsListFragmentDirections

class BarsListAdapter(
    private val pubs: List<PubRoom>,
    private val barsListFragment: BarsListFragment,
) :
    RecyclerView.Adapter<BarsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewPubName: TextView
        val textViewPeopleCount: TextView

        init {
            textViewPubName = view.findViewById(R.id.textViewPubsAroundPubName)
            textViewPeopleCount = view.findViewById(R.id.textViewPubsAroundDistance)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.bars_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pub = pubs[position]
        viewHolder.textViewPubName.text = pub.name
        viewHolder.textViewPeopleCount.text = String.format("%s: %s",
            "Počet ľudí", pub.users)

        viewHolder.textViewPubName.setOnClickListener {
            val action = BarsListFragmentDirections.actionBarsListFragmentToBarDetailFragment(
                id = pub.id,
                peoplePresentCount = pub.users,
            )
            barsListFragment.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = pubs.size
}
