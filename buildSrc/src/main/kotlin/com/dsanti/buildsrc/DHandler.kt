package com.dsanti.buildsrc

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.addComposeOfficialDependencies() {
    composeOfficialDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addComposeDebugDependencies() {
    composeDebugDependencies.forEach {
        add("debugImplementation", it)
    }
}

fun DependencyHandler.addKotlinDependencies() {
    kotlinDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addKotlinTestDependencies() {
    kotlinTestDependencies.forEach {
        add("testImplementation", it)
    }
}

fun DependencyHandler.addDataDependencies() {
    add("kapt", Dependencies.roomCompiler)
    dataDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addDiDependencies(){
    DiDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addCoreAndroidDependencies() {
    coreAndroidDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addCoreAndroidUiDependencies() {
    add("kapt", Dependencies.lifecycleCompiler)
    coreAndroidUiDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addGoogleAndroidDependencies() {
    googleAndroidLibraries.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addNetworkingDependencies() {
    networkingDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addJunit5TestDependencies() {
    junit5TestDependencies.forEach {
        add("testImplementation", it)
    }
}

fun DependencyHandler.addThirdPartyUnitTestsDependencies() {
    thirdPartyUnitTestsDependencies.forEach {
        add("testImplementation", it)
    }
}

fun DependencyHandler.addAndroidInstrumentationTestsDependencies() {
    androidInstrumentationTestsDependencies.forEach {
        add("androidTestImplementation", it)
    }
}

fun DependencyHandler.addThirdPartyUiDependencies() {
    thirdPartyUiDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addBiometricDependency() {
    add("implementation", Dependencies.biometric)
}

fun DependencyHandler.addFirebaseDependencies() {
    firebaseDependecies.forEach {
        add("implementation", platform(Dependencies.firebaseBom))
        add("implementation", it)
    }
}

fun DependencyHandler.addAccompanistDependencies() {
    accompanistDependencies.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addCameraXDependencies() {
    cameraXDependencies.forEach {
        add("implementation", it)
    }
}