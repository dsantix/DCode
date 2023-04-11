import com.dsanti.buildsrc.*


plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    namespace = "com.dsanti.dcode"
    compileSdk = Configs.compileSdkVersion

    defaultConfig {
        applicationId = Configs.applicationId
        minSdk = Configs.minSdkVersion
        targetSdk = Configs.targetSdkVersion
        versionCode = 4
        versionName = "0.21"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }

        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"

    }

    kapt {
        correctErrorTypes = true
    }

    buildFeatures {
        compose = true
        buildConfig = true

        // Disable unused AGP features
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Configs.kotlinCompilerExtensionVersion
    }
    lint {
        abortOnError = false
    }
    packagingOptions {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {

    addKotlinDependencies()

    addDataDependencies()

    //addDiDependencies()

    addComposeOfficialDependencies()
    addComposeDebugDependencies()

    addThirdPartyUiDependencies()

    addCoreAndroidDependencies()
    addCoreAndroidUiDependencies()
    addGoogleAndroidDependencies()
    addNetworkingDependencies()

    addKotlinTestDependencies()
    addJunit5TestDependencies()
    addThirdPartyUnitTestsDependencies()

    addAndroidInstrumentationTestsDependencies()
    addBiometricDependency()

    addFirebaseDependencies()
    addAccompanistDependencies()
    addCameraXDependencies()
}