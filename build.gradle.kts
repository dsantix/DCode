
buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:10.5.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.1")
        classpath("com.google.firebase:perf-plugin:1.4.2")

    }
}

allprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        google()
        maven(url = "https://jitpack.io")
        jcenter()
    }
}
