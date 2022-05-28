package com.dsanti.dcode

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.whenCreated
import com.dsanti.dcode.ui.SystemBarTransparent
import com.dsanti.dcode.ui.theme.DCodeTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.flow.*


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            DCodeTheme {
                SystemBarTransparent()
                DCodeApp()
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    @Composable
    fun DCodeApp(){
        val navController = rememberAnimatedNavController()

        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

        val topBarState = rememberSaveable { (mutableStateOf(true)) }

        val isAppUpdate = rememberSaveable {
            mutableStateOf(false)
        }

        val context = LocalContext.current

        val versionApp = intPreferencesKey("version_app")

        LaunchedEffect(Unit){
            println("Launched")
            val versionAppFlow: Flow<Int> = context.applicationContext.dataStore.data
                .map { preferences ->
                    preferences[versionApp] ?: 1
                }

            versionAppFlow.onEach {
                if (BuildConfig.VERSION_CODE> it) {
                    incrementCounter(this@MainActivity.applicationContext, versionApp, BuildConfig.VERSION_CODE)
                    isAppUpdate.value = true
                }
                println("Launched 2")
            }.launchIn(this)
        }

        val decayAnimationSpec = rememberSplineBasedDecay<Float>()
        val scrollBehavior = remember(decayAnimationSpec) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
        }


        DCodeAppNavigation(
            bottomBarState = bottomBarState,
            topBarState = topBarState,
            navController = navController
        )

        Scaffold(
            bottomBar = { BottomAppBar(context = context, navController = navController, bottomBarState = bottomBarState) },
            topBar = { TopAppBar(navController = navController, topBarState = topBarState, scrollBehavior = scrollBehavior) },
            modifier = Modifier
                .systemBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ){ padding ->
            DCodeAppContent(
                navController = navController,
                bottomBarState = bottomBarState,
                topBarState = topBarState,
                paddingValues = padding,
                isAppUpdate
            )
        }
    }
}

suspend fun incrementCounter(context:Context, key: Preferences.Key<Int>, atualVersionCode:Int) {
    context.dataStore.edit { settings ->
        settings[key] = atualVersionCode
    }
}

