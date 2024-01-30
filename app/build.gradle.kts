import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "io.silv.hsrdmgcalc"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.silv.hsrdmgcalc"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        val properties = Properties().also {
            it.load(project.rootProject.file("local.properties").inputStream())
        }


        buildTypes.onEach { buildType ->
            buildType.buildConfigField("String", "SUPABASE_API_URL", properties.getProperty("SUPABASE_API_URL"))
            buildType.buildConfigField("String", "SUPABASE_API_KEY", properties.getProperty("SUPABASE_API_KEY"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":presentation-core"))
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":coil-disk-fetcher"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation("androidx.compose.ui:ui-tooling-preview")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.manifest)

    implementation(libs.androidx.material3.window.size)

    implementation(libs.sqldelight.android.driver)
    implementation(libs.sqldelight.extension.coroutines)
    implementation(libs.sqldelight.extension.primitive.adapters)

    implementation(libs.kotlin.serialization)

    implementation(libs.supabase.storage)
    implementation(libs.supabase.gotrue.kt)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.workmanager)
    implementation(libs.koin.navigation)

    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlin.collections.immutable)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.voyager.koin)
    implementation(libs.voyager.tabNavigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenModel)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.coil.gif)
    implementation(libs.coil)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.core)
    implementation(libs.ktor.serialization.json)

// Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.gcm)
    implementation(libs.androidx.work.multiprocess)
    implementation(libs.androidx.work.testing)

    implementation(libs.haze)
}

tasks {
    // See https://kotlinlang.org/docs/reference/experimental.html#experimental-status-of-experimental-api(-markers)
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
            "-opt-in=coil.annotation.ExperimentalCoilApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
        )
    }
}