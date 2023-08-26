package com.joly.testtaks.screen

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.joly.testtaks.models.appViewModelFactory
import com.joly.testtaks.models.dataBaseViewModel
import com.joly.testtaks.models.users.User
import com.joly.testtaks.navigation.Routes
import com.joly.testtaks.titleTopApp
import com.joly.testtaks.viewModel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.internal.immutableListOf

class HomeScreen {
    private lateinit var dbviewModel: dataBaseViewModel

    @Composable
    fun UserItem(
            user: User,
            num: Int,
            navController: NavHostController
    ) {
        ListItem(
            modifier =
            Modifier
                .clickable {
                    navController.navigate(Routes.DisplayRepos + "/${user.login}")
                },
            headlineContent = { Text(text = user.login) },
            leadingContent = { Text(text = "${num})") },
            trailingContent = {
                AsyncImage(
                    model = user.avatar_url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                )
            }
        )
        Divider()
    }

    @Composable
    fun Screen(
            viewModel: AppViewModel,
            navController: NavHostController
    ) {
        titleTopApp = "Users"
        val context = LocalContext.current
        var isLoaded by remember {mutableStateOf(false)}
        val lazycolumnState = rememberLazyListState()
        var isAddLoading by remember { mutableStateOf(false) }
        dbviewModel =
            viewModel(factory = appViewModelFactory(context.applicationContext as Application))
        var page by remember { mutableStateOf(0) }
        var usersList by remember { mutableStateOf(emptyList<User>()) }
        LaunchedEffect(Unit) {
            val fromDB = dbviewModel.getAllUsers()
            usersList = fromDB
            if(usersList.isNotEmpty()) {
                println("from DB")
                usersList.forEach{
                    println(it.login)
                }
                isLoaded = true
            }
            viewModel.fetchUsers(since = 0)
            if(viewModel.users.value != null) {
                usersList = viewModel.users.value!!
            }

            if(viewModel.users.value != null) {
                usersList.forEach {
                    dbviewModel.upsertUser(it)
                }
            }
            isLoaded = true
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            if(!isLoaded){
                CircularProgressIndicator()
            } else{
                if (usersList.isEmpty()) {
                    Text("Users not found")
                } else {
                    LazyColumn(
                        state = lazycolumnState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(usersList.size) {
                            UserItem(usersList[it], it + 1, navController)
                        }
                        if(lazycolumnState.firstVisibleItemIndex + lazycolumnState.layoutInfo.visibleItemsInfo.size == usersList.size && !isAddLoading){
                            page += 1
                            println("Updating page: $page")
                            isAddLoading = true
                           val coroutineScope = CoroutineScope(Dispatchers.IO)
                            coroutineScope.launch {
                                viewModel.fetchUsers(since = 20 * (page-1))
                                if(viewModel.users.value != null) {
                                    usersList += viewModel.users.value!!
                                }
                                delay(1000)
                                isAddLoading = false
                            }
                        }
                        if (isAddLoading) {
                            item{
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
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
    }
}