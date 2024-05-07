package org.d3if3102.hitungparkir.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logKendaraan")
data class LogKendaraan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val kendaraan: String,
    val lamaParkir: String,
    val platNo: String
)
