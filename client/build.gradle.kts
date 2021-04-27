plugins {
//    id 'org.jetbrains.kotlin.frontend' version '0.0.45' apply false
    kotlin("plugin.serialization")
    kotlin("js")
}


kotlin {
    js {
        useCommonJs()
        browser {
//            @Suppress("EXPERIMENTAL_API_USAGE")
//            distribution {
//                directory = file("$projectDir/output/")
//            }
//            runTask {
//                outputFileName = "main.bundle.js"
//                sourceMaps = false
//                devServer = org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer(
//                        open = false,
//                        port = 8080,
//                        proxy = mapOf(
//                                "/*" to "http://localhost:8080"
//                    "/callback" to "http://localhost:8080",
//                    "/logout" to "http://localhost:8080",
//                    "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
//                        ),
//                        contentBase = listOf("$buildDir/processedResources/frontend/main")
//                )
//            }

        }
        binaries.executable()
//        task("run")
////        runTask
//        {
//            val outputFileName = "main.bundle.js"
//            val sourceMaps = false
//            val devServer = org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer(
//                    open = false,
//                    port = 8088,
////            proxy = mapOf(
////                    "/*" to "http://localhost:8080",
////                    "/callback" to "http://localhost:8080",
////                    "/logout" to "http://localhost:8080",
////                    "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
////            ),
//                    contentBase = listOf("$buildDir/processedResources/frontend/main")
//            )
//        }

//    webpackTask
        task("webpack") {
            val outputFileName = "main.bundle.js"
//    proxyUrl = "http://localhost:8080"
//    port = 8088
        }
    }
}

//apply plugin: 'kotlin.js'
//apply plugin: 'org.jetbrains.kotlin.frontend'
//apply plugin: 'kotlin-dce-js'

repositories {
    jcenter()
//    maven { url "https://kotlin.bintray.com/kotlin-js-wrappers" }
    mavenCentral()
}

val kotlin_react_version = "16.6.0-pre.67"
val kotlinWrappersSuffix = "pre.110-kotlin-1.4.0"

//compileKotlin2Js {
//    kotlinOptions {
//        sourceMap = true
//        moduleKind = "commonjs"
//        metaInfo = true
//        main = "call"
//    }
//}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
    implementation("org.jetbrains:kotlin-react:17.0.1-pre.148-kotlin-1.4.21")
    implementation("org.jetbrains:kotlin-react-dom:$kotlin_react_version-kotlin-1.4.30")
    implementation("org.jetbrains:kotlin-styled:1.0.0-pre.148-kotlin-1.4.21")
    implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.148-kotlin-1.4.21")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.11.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-js:1.1.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0")
//    implementation(npm("core-js", "2.6.5"))
    implementation(npm("svg-inline-loader", "0.8.0"))
//    implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlVersion")
    implementation(npm("react", "16.13.1"))
    implementation(npm("react-dom", "16.13.1"))
    implementation(npm("react-is", "16.13.1"))
    implementation("org.jetbrains:kotlin-react-dom:16.13.1-$kotlinWrappersSuffix")
    implementation("org.jetbrains:kotlin-styled:1.0.0-$kotlinWrappersSuffix")
    implementation("org.jetbrains:kotlin-extensions:1.0.1-$kotlinWrappersSuffix")
    implementation("org.jetbrains:kotlin-css:1.0.0-$kotlinWrappersSuffix")
    implementation(npm("inline-style-prefixer", "5.1.0"))
    implementation(npm("styled-components", "4.3.2"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-js")


}

//kotlinFrontend {
//    npm {
//        devDependency "karma"
//        dependency "react-router-dom"
//        dependency "@jetbrains/kotlin-css"
//        dependency "inline-style-prefixer"
//        dependency "styled-components", "^4.3.2"
//    }

//    sourceMaps = true