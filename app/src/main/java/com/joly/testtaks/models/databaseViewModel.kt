package com.joly.testtaks.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.joly.testtaks.models.repos.RepoEntyty.RepoE
import com.joly.testtaks.models.users.User

class dataBaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: dbReposytory

    init {
        val userDao = appDatabase.getInstance(application).userDao()
        val repoDao = appDatabase.getInstance(application).repoDao()
        repository = dbReposytory(userDao, repoDao)
    }

    fun getAllUsers(): List<User> {
        return repository.getAllUsers()
    }

    fun upsertUser(User: User) {
        repository.upsertUser(User)
    }

    fun deleteUser(User: User) {
        repository.deleteUser(User)
    }

    fun getUserRepos(login: String): List<RepoE> {
        return repository.getUserRepos(login)
    }

    fun upsertRepo(Repo: RepoE) {
        repository.upsertRepo(Repo)
    }

    fun deleteRepo(Repo: RepoE) {
        repository.deleteRepo(Repo)
    }

    fun replaceAllRepos(
            Repos: List<RepoE>,
            login: String
    ) {
        repository.deleteAllReposByUserId(login)
        for (repo in Repos) {
            repository.upsertRepo(repo)
        }
    }
}