package com.joly.testtaks.Api

import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.users.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetorfitService {
    @GET("users?per_page=20")
    suspend fun getUsers(@Query("since") since: Int): List<User>

    @GET("users/{login}/repos")
    suspend fun getUserRepos(@Path("login") login: String): List<Repo>
}
