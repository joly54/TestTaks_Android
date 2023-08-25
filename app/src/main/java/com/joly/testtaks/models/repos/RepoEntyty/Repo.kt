package com.joly.testtaks.models.repos.RepoEntyty

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class RepoE(
        @PrimaryKey(autoGenerate = true) val num: Int = 0,
        val login: String,
        val repoName: String,
        val mainBranche: String,
)