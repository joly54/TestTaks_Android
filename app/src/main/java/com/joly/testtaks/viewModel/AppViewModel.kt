package com.joly.testtaks.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joly.testtaks.Api.RetrofitRepository
import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.users.User

class AppViewModel: ViewModel()  {
    private val repository = RetrofitRepository()

    private val _users = MutableLiveData<List<User>>()
    private val _repos = MutableLiveData<List<Repo>>()
    val users: LiveData<List<User>> = _users
    val repos: LiveData<List<Repo>> = _repos

    suspend fun fetchUsers(since: Int) {
        try{
            val users = repository.getUsers(since)
            _users.value = users
        } catch (e: Exception){
            println(e.stackTraceToString())
        }
    }

    suspend fun fetchUserRepos(login: String) {
        try {
            val repos = repository.getUserRepos(login)
            _repos.value = repos
        } catch (e: Exception){
            println(e.stackTraceToString())
        }
    }
}