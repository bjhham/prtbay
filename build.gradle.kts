
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
}

val refType: String? = System.getenv("GITHUB_REF_TYPE")
val refName: String? = System.getenv("GITHUB_REF_NAME")

if (refName != null) {
    println("Building release $refName")
}

subprojects {
    group = "com.github.bjhham.prtbay"
    version = when {
        refType == "tag" && refName?.startsWith("release-") == true ->
            refName.removePrefix("release-")
        else -> "1.0.0-SNAPSHOT"
    }
}
