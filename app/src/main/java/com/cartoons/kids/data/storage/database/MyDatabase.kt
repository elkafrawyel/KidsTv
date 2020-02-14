package com.cartoons.kids.data.storage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cartoons.kids.data.model.PlayListDB
import com.cartoons.kids.data.model.VideoDB

@Database(entities = [PlayListDB::class, VideoDB::class], version = 1)
abstract class SleneeDatabase : RoomDatabase() {

    abstract fun dao(): MyDao

    companion object {
        @Volatile
        private var instance: SleneeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            SleneeDatabase::class.java, "slenee.db"
        ).allowMainThreadQueries().build()
    }

}