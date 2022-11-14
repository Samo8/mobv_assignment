package com.example.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.assignment.common.PubData

class PubDataViewModel: ViewModel() {
    var pubData: PubData = PubData(elements = mutableListOf())
}