import org.gradle.kotlin.dsl.implementation
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") // ksp
    alias(libs.plugins.compose.compiler) // compose compiler
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.baselineprofile) // hilt
    id("org.jlleitschuh.gradle.ktlint") // ktlint
}
val properties = Properties()
val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
val versionBuild = 0 // bump for dogfood builds, public betas, etc.
val isBeta = true

var versionExt = ""
if (versionBuild > 0) {
    versionExt += ".$versionBuild"
}
if (isBeta) {
    versionExt += "-beta"
}
android {
    namespace = "com.mostafadevo.freegames"
    compileSdk = 35

    try {
        val propertiesFile = rootProject.file("keystore.properties")
        val properties = Properties()

        if (propertiesFile.readText().isNotEmpty()) {
            FileInputStream(propertiesFile).use { inputStream ->
                properties.load(inputStream)
            }

            signingConfigs {
                getByName("debug") {
                    storeFile = file(properties["signingConfig.storeFile"] as String)
                    storePassword = properties["signingConfig.storePassword"] as String
                    keyAlias = properties["signingConfig.keyAlias"] as String
                    keyPassword = properties["signingConfig.keyPassword"] as String
                }
            }
        }
    } catch (ignored: IOException) {
        // Handle exception if necessary
    }

    defaultConfig {
        applicationId = "com.mostafadevo.freegames"
        minSdk = 24
        targetSdk = 35
        versionCode =
            versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100 + versionBuild
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}${versionExt}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ktlint {
        android = true
        ignoreFailures = true
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.HTML)
        }
    }
    splits {
        abi {
            isEnable = true
            reset()
            include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
            isUniversalApk = true
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.animation)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.coil)
    implementation(libs.volley)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.browser)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    "baselineProfile"(project(":baselineprofile"))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // timber
    implementation(libs.timber)

    // kotlin coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.viewmodel)

    // room db
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.ktx)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin.codegen)
    implementation(libs.logging.interceptor)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // datastore
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material)
    implementation (libs.androidx.core.ktx.v1120)
}
ksp {
    arg("dagger.incremental", "true")
}
