subprojects {
    apply<BasePlugin>()

    repositories {
        mavenCentral()
    }

    configure<BasePluginConvention> {
        archivesBaseName = "kormatter-$archivesBaseName"
    }
}
