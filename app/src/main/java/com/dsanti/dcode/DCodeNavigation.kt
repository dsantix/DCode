package com.dsanti.dcode

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dsanti.dcode.ui.home.Home
import com.dsanti.dcode.ui.scan.ScanActivity
import com.dsanti.dcode.ui.settings.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

val settingsItemList = listOf(SettingsItem(Icons.Rounded.History, R.string.settings_history, Screen.History),
    SettingsItem(Icons.Rounded.LocalPolice, R.string.privacy_policy, Screen.PrivacyPolicy), SettingsItem(Icons.Rounded.Info, R.string.settings_about, Screen.About)
)

@Composable
fun BottomAppBar(context: Context, navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    val items = listOf(Screen.Home, Screen.Settings)

    AnimatedVisibility(visible = bottomBarState.value,
    enter = slideInVertically(initialOffsetY = {it}),
    exit = slideOutHorizontally(targetOffsetX = {it})) {
        NavigationBar(Modifier.statusBarsPadding()) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBarItem(
                icon = { Icon(imageVector = items[0].icon, contentDescription = null) },
                selected = currentRoute == items[0].route,
                onClick = { navController.navigate(items[0].route){
                    popUpTo(navController.graph.findStartDestination().id){
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                label = { Text(text = stringResource(id = items[0].title)) },
            )
            FloatingActionButton(onClick = { context.startActivity(Intent(context, ScanActivity::class.java)) }, Modifier.padding(top = 4.dp)) {
                Icon(painter = painterResource(id = R.drawable.ic_qr_nav), contentDescription = null)
            }
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Rounded.Settings, contentDescription = null) },
                selected = currentRoute == items[1].route,
                onClick = { navController.navigate(items[1].route){
                    popUpTo(navController.graph.findStartDestination().id){
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                label = { Text(text = stringResource(items[1].title)) },
            )
        }
    }
}

@Composable
fun TopAppBar(navController: NavHostController, topBarState: MutableState<Boolean>, scrollBehavior : TopAppBarScrollBehavior) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()


    val title : Int = when(navBackStackEntry?.destination?.route ?: "home"){
        "home" -> Screen.Home.title
        "settings" -> Screen.Settings.title
        "history" -> Screen.History.title
        "about" -> Screen.About.title
        "privacyPolicy" -> Screen.PrivacyPolicy.title
        "licenses" -> Screen.Licenses.title
        "changelog" -> Screen.Changelog.title
        else -> {Screen.Home.title}
    }

    AnimatedVisibility(visible = topBarState.value,
    enter = slideInVertically(initialOffsetY = {it}),
    exit = slideOutHorizontally(targetOffsetX = {it})) {
        MediumTopAppBar(title = { Text(text = stringResource(id = title))}
        ,navigationIcon = { IconButton(onClick = { navController.popBackStack()}) {
                Icon(imageVector = Icons.Rounded.KeyboardBackspace, contentDescription = null)
            }}, scrollBehavior = scrollBehavior)
    }

}

@Composable
fun DCodeAppNavigation(bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>, navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Control TopBar and BottomBar
    when (navBackStackEntry?.destination?.route) {
        "home" -> {
            bottomBarState.value = true
            topBarState.value = false
        }
        "settings" -> {
            bottomBarState.value = true
            topBarState.value = false
        }
        "history" -> {
            bottomBarState.value = true
            topBarState.value = false
        }
        "about" -> {
            bottomBarState.value = true
            topBarState.value = false
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DCodeAppContent(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>, paddingValues: PaddingValues, isAppUpdated:MutableState<Boolean>) {

    AnimatedNavHost(navController = navController, startDestination = Screen.Home.route){
        composable(Screen.Home.route, enterTransition = {
            when(initialState.destination.route){
                Screen.Settings.route -> slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                else -> null
            }
        }, exitTransition = {
            when(targetState.destination.route){
                Screen.Home.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        }){
            LaunchedEffect(Unit){
                bottomBarState.value = true
                topBarState.value = false
            }
            Home(isAppUpdated)
        }

        composable(Screen.Settings.route, enterTransition = {
            when(initialState.destination.route){
                Screen.Home.route -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        }){
            LaunchedEffect(Unit){
                bottomBarState.value = true
                topBarState.value = false
            }
            Settings(listSettings = settingsItemList, navController = navController)
        }

        composable(Screen.About.route, exitTransition = {
            when(targetState.destination.route){
                Screen.Settings.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        }){
            LaunchedEffect(Unit){
                bottomBarState.value = false
                topBarState.value = true
            }
            About(Modifier.padding(paddingValues), navController)
        }

        composable(Screen.PrivacyPolicy.route, exitTransition = {
            when(targetState.destination.route){
                Screen.Settings.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        }){
            LaunchedEffect(Unit){
                bottomBarState.value = false
                topBarState.value = true
            }
            PrivacyPolicy()
        }

        composable(Screen.History.route, exitTransition = {
            when(targetState.destination.route){
                Screen.Settings.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        }){
            LaunchedEffect(Unit){
                bottomBarState.value = false
                topBarState.value = true
            }
            History(Modifier.padding(paddingValues))
        }

        composable(Screen.Licenses.route, exitTransition = {
            when(targetState.destination.route){
                Screen.About.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        }){
            LaunchedEffect(Unit){
                bottomBarState.value = false
                topBarState.value = true
            }
            Licenses(Modifier.padding(paddingValues))
        }

        composable(Screen.Changelog.route, exitTransition = {
            when(targetState.destination.route){
                Screen.About.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                else -> null
            }
        }){
            LaunchedEffect(Unit){
                bottomBarState.value = false
                topBarState.value = true
            }
            Changelog(Modifier.padding(paddingValues))
        }
    }
}



sealed class Screen(val route: String, @StringRes val title: Int, val icon: ImageVector){
    object Home : Screen("home", R.string.nav_home, Icons.Rounded.Home)
    object Settings : Screen("settings", R.string.settings, Icons.Rounded.Settings)
    object About : Screen("about", R.string.settings_about, Icons.Rounded.Info)
    object History : Screen("history", R.string.settings_history, Icons.Rounded.History)
    object PrivacyPolicy : Screen("privacyPolicy", R.string.privacy_policy, Icons.Rounded.Policy)
    object Licenses : Screen("licenses", R.string.about_licenses, Icons.Rounded.VpnKey)
    object Changelog : Screen("changelog", R.string.changelog, Icons.Rounded.ChangeHistory)
}