package com.example.navcontroller.activities

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navcontroller.screen.ConversationScreen
import com.example.navcontroller.screen.HomeScreen
import com.example.navcontroller.screen.SettingScreen
import com.example.navcontroller.ui.theme.NavControllerTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.navcontroller.R
import com.example.navcontroller.screen.HistoryScreen
import com.example.navcontroller.screen.LottieTranslateAnimation

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

// ---------------------- Sealed class for Bottom Navigation Tabs ----------------------

sealed class BottomNavScreen(
    val route: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    object Home : BottomNavScreen(
        "home",
        R.drawable.home_icon,
        R.drawable.home_icon_outline
    )

    object Conversation : BottomNavScreen(
        "conversation",
        R.drawable.chat_icon_filled,
        R.drawable.chat_icon_outline
    )

    object Setting : BottomNavScreen(
        "setting",
        R.drawable.setting_icon_filled,
        R.drawable.setting_icon
    )
}


// ---------------------- Main Activity ----------------------
@HiltAndroidApp
class MyApp : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavControllerTheme {
                var isAnimationDone by remember { mutableStateOf(false) }
                if (isAnimationDone) {
                    MainScreen()
                } else {
                    LottieTranslateAnimation(
                        onNavigateToAdScreen = {
                            isAnimationDone = true
                        }
                    )
                }

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Conversation,
        BottomNavScreen.Setting
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { screen ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selected) screen.selectedIcon else screen.unselectedIcon
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                label = null,
                alwaysShowLabel = false
            )
        }
    }
}



@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController) // 👈 Call your bottom nav here
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Home.route) { HomeScreen() }
            composable(BottomNavScreen.Conversation.route) { ConversationScreen (navController) }
            composable(BottomNavScreen.Setting.route) { SettingScreen (navController) }
            composable("history") { HistoryScreen(navController) }

            /*navigation(
                startDestination = "setting",
                route = BottomNavScreen.Setting.route
            ) {
                composable("setting") { SettingScreen(navController) }
                composable("history") { HistoryScreen(navController) }
            }*/
        }
    }
}


// ---------------------- Screen Composables ----------------------




/*@Composable
fun HistoryScreen(navController: androidx.navigation.NavController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Setting")
        }
    }
}*/
