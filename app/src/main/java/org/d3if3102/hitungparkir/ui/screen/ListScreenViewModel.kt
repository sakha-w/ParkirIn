package org.d3if3102.hitungparkir.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.d3if3102.hitungparkir.database.LogKendaraanDao
import org.d3if3102.hitungparkir.model.LogKendaraan

class ListScreenViewModel(private val dao: LogKendaraanDao) : ViewModel() {
    val data: StateFlow<List<LogKendaraan>> = dao.getLogKendaraan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun insert(kendaraan: String, lamaParkir: String, platNo: String) {
        val logKendaraan = LogKendaraan(
            kendaraan = kendaraan,
            lamaParkir = lamaParkir,
            platNo = platNo
        )
        viewModelScope.launch(Dispatchers.IO){
        dao.insert(logKendaraan)
        }
    }

    suspend fun getLogKendaraan(id: Long): LogKendaraan? {
        return dao.getLogKendaraanById(id)
    }

    fun update(id: Long, kendaraan: String, lamaParkir: String, platNo: String) {
        val logKendaraan = LogKendaraan(
            id = id,
            kendaraan = kendaraan,
            lamaParkir = lamaParkir,
            platNo = platNo
        )
        viewModelScope.launch(Dispatchers.IO){
        dao.update(logKendaraan)
        }
    }

    private fun getParkir(): List<LogKendaraan> {
        val data = mutableListOf<LogKendaraan>()
        for (i in 29 downTo 20) {
            data.add(
                LogKendaraan(
                    i.toLong(),
                    "asdas",
                    "asda",
                    "adsasd"
                )
            )
        }
        return data
    }
}