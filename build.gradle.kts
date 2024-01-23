// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) version libs.versions.agp.get() apply false
    alias(libs.plugins.kotlinAndroid) version libs.versions.kotlin.get() apply false
    alias(libs.plugins.androidLibrary) version libs.versions.agp.get() apply false
    alias(libs.plugins.sqldelight) version libs.versions.sqldelight.get() apply false
    alias(libs.plugins.kotlinSerialization).apply(false)
}