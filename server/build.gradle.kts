plugins {
//    id("java")
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
    id("distribution")
//    id("com.github.johnrengelman.shadow") version "5.0.0"
}


repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}


val ktorVersion = "1.5.3"
val logbackVersion = "1.2.3"

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains:kotlin-css:1.0.0-pre.110-kotlin-1.4.0")
//    implementation("com.charleskorn.kaml:kaml:0.11.0")
    implementation("org.jetbrains.exposed:exposed:0.16.1")
    implementation("org.postgresql:postgresql:42.2.2")
    implementation("javax.mail:mail:1.4.1")
//    compile files('marvin/framework/marvin_1.5.5.jar')
    implementation("com.yandex.android:disk-restapi-sdk:1.03")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0-1.4-M2")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks.withType<Copy>().named("processResources") {
    from(project(":client").tasks.named("browserDistribution"))
}

tasks.jar {
    manifest {
        attributes("Main-Class" to application.mainClassName)
    }
    from(
            configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    )
}
