plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.recycleease_customerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recycleease_customerapp"
        minSdk = 25
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
}
dependencies {
    implementation(libs.okhttp)
    implementation(libs.json)
    implementation(libs.recyclerview)


    implementation("org.osmdroid:osmdroid-android:6.1.10")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation(platform(libs.firebase.bom))  // Firebase BOM
    implementation(libs.firebase.auth)           // Firebase Authentication
    implementation(libs.firebase.database)       // Firebase Realtime Database
    implementation(libs.firebase.firestore)      // Firestore
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
