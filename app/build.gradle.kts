@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.screenshot)
    alias(libs.plugins.hiltAndroid)
    id("kotlin-kapt")
    id("jacoco")
}

android {
    namespace = "com.mevalera.feelslike"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mevalera.feelslike"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"d23b124aec4644dca72143414241812\"")
        buildConfigField("String", "BASE_URL", "\"https://api.weatherapi.com/v1/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "1.9"
    }

    experimentalProperties["android.experimental.enableScreenshotTest"] = true

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.extensions.configure(JacocoTaskExtension::class.java) {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
        }
    }

    packaging {
        resources {
            excludes.add("META-INF/LICENSE*")
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
            excludes.add("META-INF/*.kotlin_module")
            excludes.add("META-INF/versions/**")
            excludes.add("META-INF/androidx.*")
            excludes.add("META-INF/proguard/**")
            merges.add("META-INF/services/**")
        }
    }
}

dependencies {
    // Core Android & Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    kapt(libs.hilt.compiler)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Image Loading
    implementation(libs.coil.compose)

    // Local Storage
    implementation(libs.androidx.datastore.preferences)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.truth)

    // Android Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)

    // Debug Implementation
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
    screenshotTestImplementation(libs.androidx.ui.tooling)
}

jacoco {
    toolVersion = "0.8.11"
}

val exclusions =
    listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/ComposableSingletons*",
        "**/tmp/**/*.class",
        "**/App.class",
        "**/di",
        "**/hilt_aggregated_deps",
        "**/dagger",
        "**/test",
        "**/ErrorHandlingKt.class",
        "**/*Preview*",
        "**/*_*",
    )

tasks
    .withType<Test> {
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
            showStackTraces = true

            afterSuite(
                KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
                    if (desc.parent == null) {
                        println("\nTest result: ${result.resultType}")
                        println(
                            "Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped",
                        )
                    }
                }),
            )
        }
    }.configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

tasks.register<JacocoReport>("jacocoDebugCodeCoverage") {
    group = "Reporting"
    description = "Generate combined Jacoco coverage reports"

    dependsOn("testDebugUnitTest", "createDebugCoverageReport")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
    additionalSourceDirs.setFrom(files(sourceDirectories))
    classDirectories.setFrom(
        files(
            fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                exclude(exclusions)
            },
            fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                exclude(exclusions)
            },
        ),
    )
    executionData.setFrom(
        files(
            fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) },
        ),
    )
}
