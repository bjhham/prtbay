
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
}

subprojects {
    group = "com.github.bjhham.prtbay"
    version = System.getenv("RELEASE_VERSION") ?: "1.0.0-SNAPSHOT"
}
