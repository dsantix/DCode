package com.dsanti.dcode

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.dsanti.dcode.ui.theme.DCodeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DCodeTheme {
                DCodeApp()
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DCodeApp(){
        val navController = rememberNavController()

        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

        val topBarState = rememberSaveable { (mutableStateOf(true)) }

        val isAppUpdate = rememberSaveable {
            mutableStateOf(false)
        }

        val context = LocalContext.current

        val versionApp = intPreferencesKey("version_app")

        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

        LaunchedEffect(Unit){
            val versionAppFlow: Flow<Int> = context.applicationContext.dataStore.data
                .map { preferences ->
                    preferences[versionApp] ?: 1
                }

            versionAppFlow.onEach {
                if (BuildConfig.VERSION_CODE> it) {
                    incrementCounter(this@MainActivity.applicationContext, versionApp, BuildConfig.VERSION_CODE)
                    isAppUpdate.value = true
                }
            }.launchIn(this)
        }


        DCodeAppNavigation(
            bottomBarState = bottomBarState,
            topBarState = topBarState,
            navController = navController
        )

        Scaffold(
            bottomBar = { BottomAppBar(context = context, navController = navController, bottomBarState = bottomBarState) },
            topBar = { TopAppBar(navController = navController, topBarState = topBarState, scrollBehavior) },
            modifier = Modifier
                .systemBarsPadding().nestedScroll(scrollBehavior.nestedScrollConnection)
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

