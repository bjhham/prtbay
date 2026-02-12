
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

application {
    mainClass = "io.ktor.server.cio.EngineMain"
}
val copyWebDistToServerResources by tasks.registering(Copy::class) {
    dependsOn(project(":web").tasks.named("jsBrowserDistribution"))
    from(project(":web").layout.buildDirectory.dir("dist/js/productionExecutable"))
    into(layout.buildDirectory.dir("resources/main/web"))
}

tasks.named("processResources") {
    dependsOn(copyWebDistToServerResources)
}

dependencies {
    implementation(ktorLibs.server.cio)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.htmx)
    implementation(ktorLibs.server.htmlBuilder)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.statusPages)

    implementation(ktorLibs.client.core)
    implementation(ktorLibs.client.cio)

    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.htmx.html)

    implementation(libs.ksoup.kotlinxIo)
    implementation(libs.logback.classic)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
