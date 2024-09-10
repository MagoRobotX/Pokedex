plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)


}

android {
    namespace = "com.magorobot.mypokedez"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.magorobot.mypokedez"
        minSdk = 33
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
        testImplementation(libs.junit)
        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        // Retrofit with Scalar Converter
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.picasso:picasso:2.71828")
        implementation("com.squareup.picasso:picasso:2.8")

        //picasso
        implementation("com.squareup.picasso:picasso:2.8")
        implementation("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
        //sql
        implementation("androidx.room:room-runtime:2.5.1")
        implementation("androidx.room:room-ktx:2.5.1")
        implementation("androidx.core:core-ktx:1.10.1")


        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

    }
