include("platform:js", "platform:jvm", "platform:common")
rootProject.name = "kormatter"

val nodejs_plugin_version: String by settings
val atomicfu_version: String by settings
val kotlin_version: String by settings

pluginManagement {
    repositories {
        mavenLocal() //todo Remove it when the user-friendly atomicfu plugin will be released.
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if(requested.id.id == "com.moowork.node")
                useVersion(nodejs_plugin_version)
            if(requested.id.id == "kotlinx-atomicfu")
                useModule("org.jetbrains.kotlinx:atomicfu-gradle-plugin:$atomicfu_version")
            if(requested.id.id.startsWith("kotlin-platform-"))
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        }
    }
}