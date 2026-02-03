plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mubarak.thmanyah"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mubarak.thmanyah"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "services"
    productFlavors {
        create("gms") {
            dimension = "services"
            buildConfigField("String", "SERVICE_TYPE", "\"GMS\"")
        }
        create("hms") {
            dimension = "services"
            applicationIdSuffix = ".huawei"
            buildConfigField("String", "SERVICE_TYPE", "\"HMS\"")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:design"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":features:home"))
    implementation(project(":features:search"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.android.compiler)

    "gmsImplementation"(libs.gms.play.services)
    "hmsImplementation"(libs.hms.core)

    testImplementation(libs.bundles.testing)
}
