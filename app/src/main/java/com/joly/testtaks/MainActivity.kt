package com.joly.testtaks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joly.testtaks.navigation.NavClass
import com.joly.testtaks.ui.theme.TestTaksTheme
import com.joly.testtaks.viewModel.AppViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTaksTheme {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun App() {
        navController = rememberNavController()
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(this.getString(R.string.app_name))
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                NavClass().Navigation(navController = navController, viewModel = viewModel)
            }
        }
    }
}
