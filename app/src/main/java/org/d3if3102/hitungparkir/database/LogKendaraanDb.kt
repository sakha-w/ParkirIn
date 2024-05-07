package org.d3if3102.hitungparkir.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import org.d3if3102.hitungparkir.model.LogKendaraan

@Database(entities = [LogKendaraan::class], version = 1, exportSchema = false)
abstract class LogKendaraanDb : RoomDatabase() {
    abstract val dao: LogKendaraanDao

    companion object {
        @Volatile
        private var INSTANCE: LogKendaraanDb? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): LogKendaraanDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LogKendaraanDb::class.java,
                        "logKendaraan.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}