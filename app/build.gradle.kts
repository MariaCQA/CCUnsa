plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.ccunsa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ccunsa"
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
    buildFeatures {
        viewBinding= true;
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation("androidx.recyclerview:recyclerview:1.2.1") // Asegúrate de tener esta línea
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    annotationProcessor(libs.room.compiler) // Ajusta según sea necesario para Room
    //kapt(libs.room.compiler) // Si estás usando KAPT para Room
}