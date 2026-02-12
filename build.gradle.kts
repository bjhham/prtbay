
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
}

subprojects {
    group = "com.github.bjhham"
    version = "1.0.0-SNAPSHOT"
}
