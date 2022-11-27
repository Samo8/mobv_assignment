package com.example.assignment.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.DataRepository
import com.example.assignment.pub_detail.model.PubDetail
import kotlinx.coroutines.launch

class PubDetailViewModel(
    private val repository: DataRepository,
): ViewModel() {
    val user = MutableLiveData<PubDetail?>(null)

    fun fetchPubDetail(pubId: String, context: Context?) {
        viewModelScope.launch {
            repository.fetchPubDetail(
                pubId,
                { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                { user.postValue(it) }
            )
        }
    }
}