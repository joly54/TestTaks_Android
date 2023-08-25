package com.joly.testtaks.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joly.testtaks.models.repos.RepoEntyty.RepoDao
import com.joly.testtaks.models.repos.RepoEntyty.RepoE
import com.joly.testtaks.models.users.User
import com.joly.testtaks.models.users.UserDao
import kotlinx.coroutines.InternalCoroutinesApi

@Database(
    entities = [
        User::class,
        RepoE::class,
               ],
    version = 3)
abstract class appDatabase  : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao

    companion object {
        private var INSTANCE: appDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): appDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    appDatabase::class.java,
                    "DB"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}