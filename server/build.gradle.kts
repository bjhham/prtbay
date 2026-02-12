
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

application {
    mainClass = "io.ktor.server.cio.EngineMain"
}

ktor {
    fatJar {
        archiveFileName = "prtbay-server.jar"
    }
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

configure<PublishingExtension> {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/bjhham/prtbay")
            credentials {
                username = System.getenv("GH_USER")
                password = System.getenv("GH_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                url = "https://github.com/bjhham/prtbay"

                licenses {
                    license {
                        name = "GNU General Public License, Version 3"
                        url = "https://www.gnu.org/licenses/gpl-3.0.txt"
                        distribution = "repo"
                    }
                }
                scm {
                    url = "https://github.com/bjhham/prtbay.git"
                }
            }
        }
    }
}
