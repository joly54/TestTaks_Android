package com.joly.testtaks.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItems(var title: String, var icon: ImageVector, var route: String){
   /* object Users : BottomNavItems("Users", , "users")
    object Settings : BottomNavItems("Settings", Icons.Settings, "settings")*/

}