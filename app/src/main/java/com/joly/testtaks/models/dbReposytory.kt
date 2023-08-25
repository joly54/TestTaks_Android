package com.joly.testtaks.models

import com.joly.testtaks.models.repos.RepoEntyty.RepoDao
import com.joly.testtaks.models.repos.RepoEntyty.RepoE
import com.joly.testtaks.models.users.User
import com.joly.testtaks.models.users.UserDao

class dbReposytory (
    private val userDao: UserDao,
    private val repoDao: RepoDao
){
    fun getAllUsers(): List<User> {
        return userDao.getAll()
    }

    fun upsertUser(User: User){
        userDao.upsert(User)
    }

    fun deleteUser(User: User){
        userDao.delete(User)
    }

    fun getUserRepos(login: String): List<RepoE> {
        return repoDao.getByUserId(login)
    }

    fun upsertRepo(Repo: RepoE){
        repoDao.insert(Repo)
    }

    fun deleteRepo(Repo: RepoE){
        repoDao.delete(Repo)
    }

    fun deleteAllReposByUserId(login: String){
        repoDao.deleteByUserId(login)
    }
}