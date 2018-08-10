import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-platform-jvm")
    id("jacoco")
}

val klock_version: String by project
val junit_jupiter_version: String by project
val jacoco_version: String by project

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    expectedBy(project(":platform:common"))

    compile(kotlin("stdlib-jdk8"))
    compile("com.soywiz:klock:$klock_version")

    testCompile(kotlin("test-annotations-common"))
    testCompile(kotlin("test-junit5"))
    testCompile("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_version")
}

jacoco {
    toolVersion = jacoco_version
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    named<Test>("test") {
        useJUnitPlatform()
    }
}