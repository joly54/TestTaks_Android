package com.joly.testtaks.screen

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.joly.testtaks.viewModel.AppViewModel
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
                    navController.navigate(Routes.DisplayRepos + "/${user.id}")
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
        val context = LocalContext.current
        var isLoaded by remember {mutableStateOf(false)}
        dbviewModel =
            viewModel(factory = appViewModelFactory(context.applicationContext as Application))
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
                    LazyColumn() {
                        items(usersList.size) {
                            UserItem(usersList[it], it + 1, navController)
                        }
                    }
                }
            }
        }
    }
}