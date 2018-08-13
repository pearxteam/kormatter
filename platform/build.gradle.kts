subprojects {
    apply<BasePlugin>()

    repositories {
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }

    configure<BasePluginConvention> {
        archivesBaseName = "kormatter-$archivesBaseName"
    }
}
