package com.joly.testtaks.screen

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joly.testtaks.models.appViewModelFactory
import com.joly.testtaks.models.dataBaseViewModel
import com.joly.testtaks.models.repos.RepoEntyty.RepoE
import com.joly.testtaks.titleTopApp
import com.joly.testtaks.viewModel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DisplayRepos {
    private lateinit var dbviewModel: dataBaseViewModel

    @Composable
    fun Screen(login: String, viewModel: AppViewModel) {
        val repos by viewModel.repos.observeAsState(emptyList())
        var isReady by remember { mutableStateOf(true) }
        val context = LocalContext.current
        var repoList by remember { mutableStateOf(emptyList<RepoE>()) }
        var lazyListState = rememberLazyListState()
        var isAddLoading by remember { mutableStateOf(false) }
        var page by remember { mutableStateOf(1) }
        titleTopApp = "${login}'s Repos"
        dbviewModel = viewModel(factory = appViewModelFactory(context.applicationContext as Application))
        LaunchedEffect(Unit) {
            println("FRom DB:\n${dbviewModel.getUserRepos(login)}")
            isReady = false
            val fromDB = dbviewModel.getUserRepos(login)
            if (fromDB.isNotEmpty()) {
                repoList = fromDB
                repoList.forEach{
                    println(it.repoName)
                }
                isReady = true
            }
            viewModel.fetchUserRepos(login, page = 1)
            if(viewModel.repos.value != null && viewModel.repos.value!!.isNotEmpty()){
                println("Replacing")
                repoList = emptyList()
                repos.forEach{
                    repoList += RepoE(
                        login =login,
                        repoName = it.name,
                        mainBranche = it.default_branch
                    )
                }
                dbviewModel.replaceAllRepos(repoList, login)
            }
            println("finaly:")
            repoList.forEach{
                println(it.repoName)
            }
            isReady = true
        }
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(isReady.not()){
                CircularProgressIndicator()
            } else if(repoList.isEmpty()){
                Text("No Repos")
            }
            else{
                LazyColumn (
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    repoList.forEach {
                        item{
                            RepoCard(it, repoList.indexOf(it)+1)
                        }
                    }
                    if(lazyListState.firstVisibleItemIndex + lazyListState.layoutInfo.visibleItemsInfo.size == repoList.size && !isAddLoading){
                        page += 1
                        println("Updating page: $page")
                        isAddLoading = true
                        val coroutineScope = CoroutineScope(Dispatchers.Main)
                        coroutineScope.launch {
                            viewModel.fetchUserRepos(login, page = page)
                            if(viewModel.repos.value != null) {
//                                repoList = emptyList()
                                repos.forEach{
                                    repoList += RepoE(
                                        login =login,
                                        repoName = it.name,
                                        mainBranche = it.default_branch
                                    )
                                }
                            }
                            delay(1000)
                            isAddLoading = false
                        }
                    }
                    if(isAddLoading){
                        item{
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ){
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private @Composable
    fun RepoCard(it: RepoE, num: Int) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            contentAlignment = Alignment.TopCenter
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ){
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    headlineContent = { Text(it.repoName) },
                    supportingContent = { Text(it.mainBranche) },
                    leadingContent = { Text(text = "${num})") },
                )
            }
        }
    }
}