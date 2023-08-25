package com.joly.testtaks.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joly.testtaks.api.RetrofitRepository
import com.joly.testtaks.models.dataBaseViewModel
import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.users.User

class AppViewModel: ViewModel()  {
    private val repository = RetrofitRepository()
    private lateinit var dbviewModel: dataBaseViewModel


    private val _users = MutableLiveData<List<User>>()
    private val _repos = MutableLiveData<List<Repo>>()
    val users: LiveData<List<User>> = _users
    val repos: LiveData<List<Repo>> = _repos

    suspend fun fetchUsers(since: Int) {
        try{
            _users.value = repository.getUsers(since)
        } catch (e: Exception){
            println(e.stackTraceToString())
        }
    }

    suspend fun fetchUserRepos(login: String) {
        try {
            _repos.value = emptyList()
            _repos.value = repository.getUserRepos(login)
        } catch (e: Exception){
            println(e.stackTraceToString())
        }
    }
}