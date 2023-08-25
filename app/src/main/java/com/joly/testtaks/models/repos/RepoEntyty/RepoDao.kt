package com.joly.testtaks.models.repos.RepoEntyty

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RepoDao {
    @Query("SELECT * FROM repos")
    fun getAll(): List<RepoE>

    @Query("SELECT * FROM repos WHERE login = :login")
    fun getByUserId(login: String): List<RepoE>

    @Upsert
    fun insert(repo: RepoE)

    @Delete
    fun delete(repo: RepoE)

    @Query("DELETE FROM repos WHERE login = :login")
    fun deleteByUserId(login: String)
    
}