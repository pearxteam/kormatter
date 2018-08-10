subprojects {
    apply<BasePlugin>()

    repositories {
        mavenLocal() //todo Remove it when the user-friendly atomicfu plugin will be released.
        mavenCentral()
    }

    configure<BasePluginConvention> {
        archivesBaseName = "kormatter-$archivesBaseName"
    }
}
