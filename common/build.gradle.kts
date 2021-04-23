plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {

    jvm()
//    {
//        tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).all {
//            kotlinOptions {
//                freeCompilerArgs += ['-Xuse-experimental=kotlin.Experimental']
//            }
//        }
//    }

    js {
        browser {}
        nodejs {}
    }
//    {
//        compilations.all {
//            kotlinOptions {
//                sourceMap = true
//                moduleKind = "commonjs"
//                metaInfo = true
//                freeCompilerArgs += ['-Xuse-experimental=kotlin.Experimental']
//            }
//        }
//
//        compilations.main {
//            kotlinOptions {
//                main = "call"
//            }
//        }
//    }

    sourceSets {
//        all {
//            kotlinOptions {
//                useIR = true
//            }
//        }
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0-1.4-M2")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
            }
        }
    }
}

repositories { 
    jcenter()
    mavenCentral()
}
