plugins {
    id("kotlin-platform-js")
}

val klock_version: String by project

dependencies {
    compile(kotlin("stdlib-js"))
    testCompile(kotlin("test-js"))
    expectedBy(project(":platform:common"))

    compile("com.soywiz:klock-js:$klock_version")
}