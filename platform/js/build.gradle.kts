import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

plugins {
    id("kotlin-platform-js")
    id("com.moowork.node")
}

val klock_version: String by project
val nodejs_version: String by project
val npm_version: String by project

configure<NodeExtension> {
    version = nodejs_version
    npmVersion = npm_version
    download = true
}

dependencies {
    expectedBy(project(":platform:common"))

    compile(kotlin("stdlib-js"))
    compile("com.soywiz:klock-js:$klock_version")

    testCompile(kotlin("test-js"))
}

tasks {
    withType<Kotlin2JsCompile> {
        kotlinOptions.moduleKind = "umd"
    }
    create<Sync>("syncNodeModules") {
        dependsOn("compileKotlin2Js")
        from(sourceSets["main"].output)
        configurations["testCompile"].forEach { from(zipTree(it)) }
        include { it.path.endsWith(".js", true) }
        into("$buildDir/node_modules")
    }
    create<NpmTask>("installMocha") {
        setArgs(listOf("install", "mocha"))
    }
    val compileTest = getByName<Kotlin2JsCompile>("compileTestKotlin2Js")
    create<NodeTask>("runMocha") {
        dependsOn("installMocha", "syncNodeModules", "compileTestKotlin2Js")
        setScript(file("node_modules/mocha/bin/mocha"))
        setArgs(listOf(compileTest.destinationDir))
    }
    named<Test>("test") {
        dependsOn("runMocha")
    }
}