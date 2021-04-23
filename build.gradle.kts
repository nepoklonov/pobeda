plugins {
    kotlin("multiplatform") version "1.4.32" apply false
    kotlin("plugin.serialization") version "1.4.32" apply false
}

allprojects {
    version = "0.1.1"
    repositories {
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }
}