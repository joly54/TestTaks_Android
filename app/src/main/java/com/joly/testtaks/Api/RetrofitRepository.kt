package com.joly.testtaks.Api

import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.users.User

class RetrofitRepository {
    private val userService = RetrofitInstance.retorfitService

    suspend fun getUsers(since: Int): List<User> {
        return userService.getUsers(since)
    }

    suspend fun getUserRepos(login: String): List<Repo> {
        return userService.getUserRepos(login)
    }
}