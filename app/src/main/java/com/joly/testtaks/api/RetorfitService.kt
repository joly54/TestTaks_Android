package com.joly.testtaks.api

import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.users.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetorfitService {
    @GET("users")
    suspend fun getUsers(@Query("since") since: Int, @Query("per_page") per_page : Int): List<User>

    @GET("users/{login}/repos")
    suspend fun getUserRepos(
            @Path("login") login: String,
            @Query("per_page") per_page: Int,
            @Query("page") page: Int,
    ): List<Repo>
}
