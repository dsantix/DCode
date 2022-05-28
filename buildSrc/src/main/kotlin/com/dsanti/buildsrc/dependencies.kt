package com.dsanti.buildsrc

object Versions {
    const val ktlint = "0.45.2"
}

object Libs {
    const val latestAboutLibsRelease = "10.1.0"
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.1"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    const val aboutLibraries = "com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:$latestAboutLibsRelease"
    const val aboutLibrariesCore = "com.mikepenz:aboutlibraries-core:$latestAboutLibsRelease"
    const val googleServices = "com.google.gms:google-services:4.3.10"
    const val crashlytics_firebase = "com.google.firebase:firebase-crashlytics-gradle:2.8.1"
    const val firebase_pref = "com.google.firebase:perf-plugin:1.4.1"

    const val junit = "junit:junit:4.13"

    const val material3 = "com.google.android.material:material:1.6.0-alpha03"

    object Kotlin {
        private const val version = "1.6.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:30.0.2"
        const val firebase_analytics_ktx = "com.google.firebase:firebase-analytics-ktx"
        const val fibase_crashlytics = "com.google.firebase:firebase-crashlytics"
        const val firebase_pref = "com.google.firebase:firebase-perf"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.4.0"
        }

        object Compose {
            const val snapshot = ""
            const val version = "1.2.0-beta01"
            const val accompanist_version = "0.24.7-alpha"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
            const val uiText = "androidx.compose.ui:ui-text-google-fonts:$version"
            const val accompanistFlowLayout = "com.google.accompanist:accompanist-flowlayout:$accompanist_version"
            const val accompanistUiController = "com.google.accompanist:accompanist-systemuicontroller:$accompanist_version"
            const val accompanistPager = "com.google.accompanist:accompanist-pager:$accompanist_version"
            const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:$accompanist_version"
            const val accompanistNavigationAnimation = "com.google.accompanist:accompanist-navigation-animation:$accompanist_version"
            const val accompanistWebView = "com.google.accompanist:accompanist-webview:$accompanist_version"


            object Material3 {
                const val snapshot = ""
                const val version = "1.0.0-alpha12"

                const val material3 = "androidx.compose.material3:material3:$version"
            }


        }

        object Room {
            const val version = "2.4.2"

            const val room = "androidx.room:room-runtime:$version"
            const val room_annotation = "androidx.room:room-compiler:$version"
        }

        object Navigation {
            private const val version = "2.4.2"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val compose = "androidx.navigation:navigation-compose:$version"
        }

        object CameraX {
            private const val version = "1.1.0-beta03"
            const val camera_core = "androidx.camera:camera-core:$version"
            const val camera_camera2 = "androidx.camera:camera-camera2:$version"
            const val camera_lifecycle = "androidx.camera:camera-lifecycle:$version"
            const val camera_video = "androidx.camera:camera-video:$version"
            const val camera_view = "androidx.camera:camera-view:$version"
            const val camera_extensions = "androidx.camera:camera-extensions:$version"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }

        object Lifecycle {
            private const val version = "2.4.1"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }
    }

    object QRCode {
        private const val version = "4.3.0"
        private const val google_version = "3.5.0"

        const val zxingAndroid = "com.journeyapps:zxing-android-embedded:$version"
        const val zxingCore = "com.google.zxing:core:$google_version"
    }

    object UI {
        private const val font_version = "1.1.0"
        private const val coil_version = "2.0.0-rc03"

        const val fontAwesome = "com.github.Gurupreet:FontAwesomeCompose:$font_version"
        const val coil_kt = "io.coil-kt:coil-compose:$coil_version"
        const val about_libraries_ui = "com.mikepenz:aboutlibraries-compose:$latestAboutLibsRelease"
        const val crop_imager = "com.github.CanHub:Android-Image-Cropper:4.2.1"
        const val capturable = "dev.shreyaspatil:capturable:1.0.3"
    }


}

object Urls {
    const val composeSnapshotRepo = "https://androidx.dev/snapshots/builds/" +
        "${Libs.AndroidX.Compose.snapshot}/artifacts/repository/"
    const val composeMaterial3SnapshotRepo = "https://androidx.dev/snapshots/builds/" +
            "${Libs.AndroidX.Compose.Material3.snapshot}/artifacts/repository/"
    const val accompanistSnapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots"
}
