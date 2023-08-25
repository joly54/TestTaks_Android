package com.joly.testtaks.models.users

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {
    @Query("SELECT * FROM Users")
    fun getAll(): List<User>

    @Upsert
    fun upsert(user: User)

    @Delete
    fun delete(user: User)
}