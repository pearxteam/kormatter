import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jdk_version: String by project
val jdk_version_short: String by project

plugins {
    id("kotlin-platform-jvm")
}

dependencies {
    testCompile(kotlin("test-junit"))
    expectedBy(project(":platform:common"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = jdk_version
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(jdk_version)
}
