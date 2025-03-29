    plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.atubu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.atubu"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        jvmTarget = "11"
    }



    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation ("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation(libs.androidx.ui.tooling.preview.android)
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val nav_version = "2.8.9"

    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")
}