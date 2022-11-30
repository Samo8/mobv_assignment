package com.example.assignment.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.ui.viewmodels.PubsAroundViewModel
import com.example.assignment.ui.viewmodels.data.PubAround

class PubsAroundAdapter(
    private val pubs: List<PubAround>,
    private val pubsAroundViewModel: PubsAroundViewModel,
) :
    RecyclerView.Adapter<PubsAroundAdapter.ViewHolder>() {
//    private val pubs = pubsAroundViewModel.pubsAround

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewPubName: TextView
        val textViewPubDistance: TextView

        init {
            textViewPubName = view.findViewById(R.id.textViewPubsAroundPubName)
            textViewPubDistance = view.findViewById(R.id.textViewPubsAroundDistance)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pubs_around_item, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pub = pubs[position]

        viewHolder.itemView.setOnClickListener {
            pubsAroundViewModel.updateIsSelected(pub.element.id)
            notifyDataSetChanged()
        }
        viewHolder.textViewPubName.text = pub.element.tags.name

        if (pub.element.id == pubsAroundViewModel.selectedPubId) {
            viewHolder.itemView.setBackgroundColor(Color.GREEN)
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE)
        }

        viewHolder.textViewPubDistance.text = String.format("%.2f", pub.distance)
    }

    override fun getItemCount() = pubs.size
}
