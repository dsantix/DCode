package com.dsanti.dcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.dsanti.dcode.ui.SystemBarTransparent
import com.dsanti.dcode.ui.theme.DCodeTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            DCodeTheme() {
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

        val context = LocalContext.current

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
                paddingValues = padding
            )
        }
    }

    @Preview
    @Composable
    fun AppPreview() {
        DCodeTheme {
            DCodeApp()
        }
    }
}
