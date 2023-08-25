package com.joly.testtaks.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.joly.testtaks.models.users.User
import com.joly.testtaks.navigation.Routes
import com.joly.testtaks.viewModel.AppViewModel

class HomeScreen {

    @Composable
    fun UserItem(user: User, num: Int, navController: NavHostController){
        ListItem(
            modifier =
                Modifier
                    .clickable {
                        navController.navigate(Routes.DisplayRepos + "/${user.id}")
                    }
            ,
            headlineContent = { Text(text = user.login)},
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
    ){
        val users by viewModel.users.observeAsState(emptyList())
        LaunchedEffect(Unit) {
            viewModel.fetchUsers(since = 0)
        }
        Column(
            modifier = Modifier
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            if(users.isEmpty()){
                CircularProgressIndicator()
            } else{
                LazyColumn(){
                    items(users.size){
                        UserItem(users[it], it+1, navController)
                    }
                }
            }
        }
    }
}