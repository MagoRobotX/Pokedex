plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")


}

android {
    namespace = "com.magorobot.mypokedez"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.magorobot.mypokedez"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true

    }


}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.tracing.perfetto.handshake)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.squareup.picasso:picasso:2.8")

    //picasso
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    //sql
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("androidx.room:room-compiler:2.4.3")
    annotationProcessor("androidx.room:room-compiler:2.5.0")
// Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
