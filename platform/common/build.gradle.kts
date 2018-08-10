plugins {
    id("kotlin-platform-common")
}

val klock_version: String by project

dependencies {
    compile(kotlin("stdlib-common"))
    compile("com.soywiz:klock-common:$klock_version")

    testCompile(kotlin("test-common"))
    testCompile(kotlin("test-annotations-common"))
}