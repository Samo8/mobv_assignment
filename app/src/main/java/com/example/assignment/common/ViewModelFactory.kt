package com.example.assignment.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignment.data.DataRepository
import com.example.assignment.ui.viewmodels.*

class ViewModelFactory(
    private val repository: DataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(BarsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarsViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddFriendViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(FriendListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendListViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(PubDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PubDetailViewModel(repository) as T
        }
//
//        if (modelClass.isAssignableFrom(LocateViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return LocateViewModel(repository) as T
//        }
//
//        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return DetailViewModel(repository) as T
//        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}