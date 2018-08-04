import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.utils.addToStdlib.cast

plugins {
    id("kotlin-platform-js")
    id("com.moowork.node")
}

val klock_version: String by project
val nodejs_version: String by project
val npm_version: String by project

dependencies {
    compile(kotlin("stdlib-js"))
    testCompile(kotlin("test-js"))
    expectedBy(project(":platform:common"))

    compile("com.soywiz:klock-js:$klock_version")
}

/*configure<NodeExtension> {
    setNodeVersion(nodejs_version)
}*/

configure<NodeExtension> {
    version = nodejs_version
    npmVersion = npm_version
    download = true
}

tasks {
    withType<Kotlin2JsCompile> {
        kotlinOptions.moduleKind = "umd"
    }
    "syncNodeModules"(Sync::class) {
        dependsOn("compileKotlin2Js")
        from(java.sourceSets["main"].output)
        configurations["testCompile"].forEach { from(zipTree(it)) }
        include { it.path.endsWith(".js", true) }
        into("$buildDir/node_modules")
    }
    "installMocha"(NpmTask::class) {
        setArgs(listOf("install", "mocha"))
    }
    "runMocha"(NodeTask::class) {
        dependsOn("installMocha", "syncNodeModules", "compileTestKotlin2Js")
        setScript(file("node_modules/mocha/bin/mocha"))
        setArgs(listOf((getByName("compileTestKotlin2Js") as Kotlin2JsCompile).destinationDir))
    }
    "test" {
        dependsOn("runMocha")
    }
}