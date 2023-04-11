
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
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:10.5.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.4")
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
