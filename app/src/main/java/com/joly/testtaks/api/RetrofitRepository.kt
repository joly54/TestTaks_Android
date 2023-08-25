package com.joly.testtaks.api

import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.users.User

class RetrofitRepository {
    private val userService = RetrofitInstance.retorfitService

    suspend fun getUsers(since: Int, per_page: Int = 20): List<User> {
        return userService.getUsers(since, per_page)
    }

    suspend fun getUserRepos(login: String): List<Repo> {
        return userService.getUserRepos(login)
    }
}