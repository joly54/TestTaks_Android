package com.joly.testtaks.screen

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.joly.testtaks.models.repos.Repo
import com.joly.testtaks.models.repos.RepoEntyty.RepoE
import com.joly.testtaks.viewModel.AppViewModel

class DisplayRepos {
    private lateinit var dbviewModel: dataBaseViewModel

    @Composable
    fun Screen(login: String, viewModel: AppViewModel) {
        val repos by viewModel.repos.observeAsState(emptyList())
        var isLoading by remember { mutableStateOf(false) }
        val context = LocalContext.current
        var repoList by remember { mutableStateOf(emptyList<RepoE>()) }
        dbviewModel =
            viewModel(factory = appViewModelFactory(context.applicationContext as Application))
        LaunchedEffect(Unit) {
            isLoading = true
            val fromDB = dbviewModel.getUserRepos(login)
            if (fromDB.isNotEmpty()) {
                repoList = fromDB
                isLoading = false
            }
            viewModel.fetchUserRepos(login)
            repoList = emptyList()
            repos.forEach{
                repoList += RepoE(
                    login =login,
                    repoName = it.name,
                    mainBranche = it.default_branch
                )
            }
                dbviewModel.replaceAllRepos(repoList, login)
            isLoading = false
        }
        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(isLoading){
                CircularProgressIndicator()
            } else if(repos.isEmpty()){
                Text("No Repos")
            }
            else{
                repos.forEach {
                    RepoCard(it, repos.indexOf(it)+1)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private @Composable
    fun RepoCard(it: Repo, num: Int) {
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            contentAlignment = Alignment.TopCenter
        ){
            Card(
                onClick = {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(it.html_url)
                    context.startActivity(openURL)

                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ){
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    headlineContent = { Text(it.name) },
                    supportingContent = { Text(it.default_branch) },
                    leadingContent = { Text(text = "${num})") },
                )
            }
        }
    }
}