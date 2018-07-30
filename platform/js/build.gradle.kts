plugins {
    id("kotlin-platform-js")
}

dependencies {
    compile(kotlin("stdlib-js"))
    testCompile(kotlin("test-js"))
    expectedBy(project(":platform:common"))
}