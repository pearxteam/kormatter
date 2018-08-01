import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-platform-jvm")
}

val jdk_version: String by project
val jdk_version_short: String by project
val klock_version: String by project

dependencies {
    compile(kotlin("stdlib-jdk$jdk_version_short"))
    testCompile(kotlin("test-junit5"))
    testCompile(kotlin("test-annotations-common"))
    testCompile(kotlin("test-common"))
    expectedBy(project(":platform:common"))

    compile("com.soywiz:klock:$klock_version")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = jdk_version
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(jdk_version)
}
