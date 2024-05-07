package org.d3if3102.hitungparkir.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3102.hitungparkir.database.LogKendaraanDao
import org.d3if3102.hitungparkir.ui.screen.ListScreenViewModel

class ViewModelFactory(
    private val dao: LogKendaraanDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListScreenViewModel::class.java)){
            return ListScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}