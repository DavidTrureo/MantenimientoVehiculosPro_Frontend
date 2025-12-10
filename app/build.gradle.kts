plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mantenimientovehiculospro"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mantenimientovehiculospro"
        minSdk = 33
        targetSdk = 36
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

    // --- NUEVO BLOQUE AGREGADO PARA CORREGIR WARNINGS DE PRUEBAS ---
    // --- BLOQUE CORREGIDO ---
    testOptions {
        unitTests {
            // Ayuda a que las pruebas no fallen por usar métodos de Android no mockeados
            isReturnDefaultValues = true
            all {
                // CORRECCIÓN: Usamos 'it.jvmArgs' en lugar de solo 'jvmArgs'
                it.jvmArgs("-XX:+EnableDynamicAgentLoading")
            }
        }
    }
    // ------------------------
}

dependencies {
    // --- Core Android ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // --- Jetpack Compose ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")

    // --- UI Architecture ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // --- Hilt ---
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // --- Data & Networking ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.androidx.datastore.preferences)

    // --- Room ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // --- QR Generation ---
    implementation(libs.zxing.core)

    // --- QR Scanning (CameraX + ML Kit) ---
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.mlkit.barcode.scanning)



    // --- INICIO DEL BLOQUE DE PRUEBAS ---
    // JUnit 4 (Generalmente ya viene por defecto, verifica que esté)
    testImplementation("junit:junit:4.13.2")

    // NUEVO: MockK para simular objetos en Kotlin
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.mockk:mockk-jvm:1.13.8") // Agrega esta si no la tienes

    // NUEVO: Librería para probar corrutinas (necesario para ViewModels)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // --- FIN DEL BLOQUE DE PRUEBAS ---

    // (mantén el resto de dependencias que tenías abajo, como las de androidTest)
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")



    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // --- Debugging ---
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}