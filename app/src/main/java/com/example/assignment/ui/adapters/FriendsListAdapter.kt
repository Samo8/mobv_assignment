package com.example.assignment.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.data.api.Friend
import com.example.assignment.ui.fragments.FriendsListFragmentDirections

class FriendsListAdapter(
    private val friends: List<Friend>,
    private val navController: NavController,
) :
RecyclerView.Adapter<FriendsListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewFriendName: TextView
        val textViewFriendBarName: TextView

        init {
            textViewFriendName = view.findViewById(R.id.textViewFriendName)
            textViewFriendBarName = view.findViewById(R.id.textViewFriendBarName)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.friend_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val friend = friends[position]

        viewHolder.textViewFriendName.text = friend.user_name
        viewHolder.textViewFriendBarName.text = friend.bar_name

        viewHolder.itemView.setOnClickListener {
            val action = FriendsListFragmentDirections.actionFriendsListFragmentToBarDetailFragment(
                id = friend.bar_id!!,
            )
            navController.navigate(action)
        }
    }

    override fun getItemCount() = friends.size
}
