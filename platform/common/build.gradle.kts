plugins {
    id("kotlin-platform-common")
}

val klock_version: String by project
val atomicfu_version: String by project

dependencies {
    compile(kotlin("stdlib-common"))
    compile("com.soywiz:klock-common:$klock_version")
    compile("org.jetbrains.kotlinx:atomicfu-common:$atomicfu_version")

    testCompile(kotlin("test-common"))
    testCompile(kotlin("test-annotations-common"))
}