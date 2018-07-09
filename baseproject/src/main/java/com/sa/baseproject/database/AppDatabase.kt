package com.sa.baseproject.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.sa.baseproject.database.daos.AppDao
import com.sa.baseproject.database.entities.SourcesItem

/**
 * @author ihsan on 12/19/17.
 */

@Database(entities = [(SourcesItem::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
