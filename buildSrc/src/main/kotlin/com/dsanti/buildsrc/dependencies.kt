package com.dsanti.buildsrc

object Dependencies {

    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val kotlinStandardLibraryJdk8 =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val androidCoreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val androidAppCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val androidPaletteKtx = "androidx.palette:palette-ktx:${Versions.paletteKtx}"
    const val googleMaterial = "com.google.android.material:material:${Versions.material}"
    const val androidMultiDex = "androidx.multidex:multidex:${Versions.multidex}"
    const val playServicesAds =
        "com.google.android.gms:play-services-ads:${Versions.playServicesAds}"

    const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeMaterial3 = "androidx.compose.material3:material3:${Versions.material3}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
    const val composeUiUtil = "androidx.compose.ui:ui-util:${Versions.compose}"
    const val composeViewBinding = "androidx.compose.ui:ui-viewbinding:${Versions.compose}"
    const val runtime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val composeMaterialIconsExtended =
        "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val composeRuntimeLivedata =
        "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    const val composeConstraintLayout =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayoutCompose}"
    const val composePaging = "androidx.paging:paging-compose:${Versions.pagingCompose}"
    const val composeViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.androidLifecycleGrouped}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.navCompose}"
    const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navCompose}"

    const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"

    const val androidPagingRuntime = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
    const val coilCompose = "io.coil-kt:coil-compose:${Versions.coilCompose}"
    const val crop_imager = "com.github.CanHub:Android-Image-Cropper:${Versions.cropImager}"
    const val capturable = "dev.shreyaspatil:capturable:${Versions.capturable}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val viewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidLifecycleGrouped}"
    const val liveDataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.androidLifecycleGrouped}"
    const val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidLifecycleGrouped}"
    const val lifecycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${Versions.androidLifecycleGrouped}"
    const val lifecycleProcess =
        "androidx.lifecycle:lifecycle-process:${Versions.androidLifecycleGrouped}"
    const val lifecycleSavedState =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.androidLifecycleGrouped}"
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junitJupiterApi}"
    const val junitJupiterEngine =
        "org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiterEngine}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val kotlinJunit5 = "org.jetbrains.kotlin:kotlin-test-junit5:${Versions.kotlin}"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    const val androidXJunit = "androidx.test.ext:junit:${Versions.androidXJunit}"

    const val biometric = "androidx.biometric:biometric:${Versions.biometric}"

    const val googleFonts = "androidx.compose.ui:ui-text-google-fonts:${Versions.googleFonts}"
    const val fontAwesome = "com.github.Gurupreet:FontAwesomeCompose:${Versions.fontAwesome}"

    const val ktorCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorClient = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val ktorSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"

    const val koin = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"

    const val aboutLibCompose = "com.mikepenz:aboutlibraries-compose:${Versions.aboutLibs}"
    const val aboutLib = "com.mikepenz:aboutlibraries-core:${Versions.aboutLibs}"

    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBOM}"
    const val firebaseAnalytics_ktx = "com.google.firebase:firebase-analytics-ktx"
    const val fibaseCrashlytics = "com.google.firebase:firebase-crashlytics"
    const val firebasePref = "com.google.firebase:firebase-perf"

    const val accompanistFlowLayout = "com.google.accompanist:accompanist-flowlayout:${Versions.accompanist}"
    const val accompanistUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
    const val accompanistPager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"
    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"
    const val accompanistNavigationAnimation = "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}"
    const val accompanistWebView = "com.google.accompanist:accompanist-webview:${Versions.accompanist}"

    const val camera_core = "androidx.camera:camera-core:${Versions.cameraX}"
    const val camera_camera2 = "androidx.camera:camera-camera2:${Versions.cameraX}"
    const val camera_lifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraX}"
    const val camera_video = "androidx.camera:camera-video:${Versions.cameraX}"
    const val camera_view = "androidx.camera:camera-view:${Versions.cameraX}"
    const val camera_extensions = "androidx.camera:camera-extensions:${Versions.cameraX}"

    const val zxingAndroid = "com.journeyapps:zxing-android-embedded:${Versions.zxing}"
    const val zxingCore = "com.google.zxing:core:${Versions.zxingGoogle}"



}


internal val composeOfficialDependencies = listOf(
    Dependencies.composeUi,
    Dependencies.composeUiTooling,
    Dependencies.composeMaterial,
    Dependencies.composeMaterial3,
    Dependencies.composeMaterialIconsExtended,
    Dependencies.composeRuntimeLivedata,
    Dependencies.composeConstraintLayout,
    Dependencies.composePaging,
    Dependencies.composeViewModel,
    Dependencies.composeActivity,
    Dependencies.composeNavigation,
    Dependencies.composeViewBinding,
    Dependencies.composeFoundation,
    Dependencies.composeLayout,
    Dependencies.composeUiUtil
)

internal val firebaseDependecies = listOf(
    Dependencies.firebaseAnalytics_ktx,
    Dependencies.fibaseCrashlytics,
    Dependencies.firebasePref
)

internal val composeDebugDependencies = listOf(
    Dependencies.kotlinReflect,
    Dependencies.composeUiTooling,
    Dependencies.composeUiTestManifest
)

internal val kotlinDependencies = listOf(
    Dependencies.kotlinStandardLibraryJdk8,
    Dependencies.coroutinesCore,
    Dependencies.coroutinesAndroid
)

internal val kotlinTestDependencies = listOf(
    Dependencies.kotlinTest,
    Dependencies.kotlinJunit5
)

internal val dataDependencies = listOf(
    Dependencies.roomRuntime,
    Dependencies.roomKtx,
    Dependencies.androidPagingRuntime,
    Dependencies.dataStore
)

internal val DiDependencies = listOf(
    Dependencies.koin,
    Dependencies.koinAndroid
)

internal val coreAndroidDependencies = listOf(
    Dependencies.androidMultiDex,
    Dependencies.androidCoreKtx,
    Dependencies.androidAppCompat
)

internal val coreAndroidUiDependencies = listOf(
    Dependencies.googleMaterial,
    Dependencies.googleFonts,
    Dependencies.androidPaletteKtx,
    Dependencies.androidPagingRuntime,
    Dependencies.lifecycleRuntimeKtx,
    Dependencies.lifecycleSavedState,
    Dependencies.lifecycleProcess,
    Dependencies.liveDataKtx,
    Dependencies.viewModelKtx,
    Dependencies.uiKtx
)

internal val googleAndroidLibraries = listOf(
    Dependencies.playServicesAds
)

internal val networkingDependencies = listOf(
    Dependencies.gson,
    Dependencies.ktorCore,
    Dependencies.ktorClient,
    Dependencies.ktorSerialization
)

internal val junit5TestDependencies = listOf(
    Dependencies.junitJupiterApi,
    Dependencies.junitJupiterEngine
)

internal val thirdPartyUnitTestsDependencies = listOf(
    Dependencies.truth
)

internal val androidInstrumentationTestsDependencies = listOf(
    Dependencies.composeUiTestJunit4,
    Dependencies.composeActivity,
    Dependencies.androidXJunit,
    Dependencies.composeUiTestManifest
)

internal val thirdPartyUiDependencies = listOf(
    Dependencies.coilCompose,
    Dependencies.aboutLib,
    Dependencies.aboutLibCompose,
    Dependencies.zxingCore,
    Dependencies.zxingAndroid,
    Dependencies.fontAwesome,
    Dependencies.crop_imager,
    Dependencies.capturable
)

internal val accompanistDependencies = listOf(
    Dependencies.accompanistFlowLayout,
    Dependencies.accompanistUiController,
    Dependencies.accompanistPager,
    Dependencies.accompanistPermissions,
    Dependencies.accompanistNavigationAnimation,
    Dependencies.accompanistWebView
)

internal val cameraXDependencies = listOf(
    Dependencies.camera_core,
    Dependencies.camera_camera2,
    Dependencies.camera_lifecycle,
    Dependencies.camera_video,
    Dependencies.camera_view,
    Dependencies.camera_extensions
)

