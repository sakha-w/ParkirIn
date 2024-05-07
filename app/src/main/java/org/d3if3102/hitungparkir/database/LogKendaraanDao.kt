package org.d3if3102.hitungparkir.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3102.hitungparkir.model.LogKendaraan


@Dao
interface LogKendaraanDao {
    @Insert
    suspend fun insert(logKendaraan: LogKendaraan)

    @Update
    suspend fun update(logKendaraan: LogKendaraan)

    @Query("SELECT * FROM logKendaraan ORDER BY id")
    fun getLogKendaraan(): Flow<List<LogKendaraan>>

    @Query("SELECT * FROM logKendaraan WHERE id = :id")
    suspend fun getLogKendaraanById(id: Long): LogKendaraan?

    @Query("DELETE FROM logKendaraan WHERE id = :id")
    suspend fun deleteById(id: Long)
}