package com.joly.testtaks.api

import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.users.User

class RetrofitRepository {
    private val userService = RetrofitInstance.retorfitService

    suspend fun getUsers(since: Int, per_page: Int = 20): List<User> {
        return userService.getUsers(since, per_page)
    }

    suspend fun getUserRepos(login: String, per_page: Int, page:Int): List<Repo> {
        return userService.getUserRepos(login, per_page, page)
    }
}