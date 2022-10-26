package com.example.assignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.common.BarData
import com.example.assignment.common.Element
import kotlinx.parcelize.Parcelize

class BarsListAdapter(
    private val elements: MutableList<Element>,
    private val barsListFragment: BarsListFragment,
) :
    RecyclerView.Adapter<BarsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.bars_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val tags = elements[position].tags
        viewHolder.textView.text = tags.name

        viewHolder.textView.setOnClickListener {
            val action = BarsListFragmentDirections.actionBarsListFragmentToBarDetailFragment(
                tags = tags,
                removeItem = RemoveItemHelper (
                    { removeBarByPosition(position) },
                    position
                )
            )
            barsListFragment.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = elements.size

    private fun removeBarByPosition(index: Int) {
        elements.removeAt(index)
        notifyItemRemoved(index)
    }
}
