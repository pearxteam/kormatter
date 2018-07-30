evaluationDependsOnChildren()
subprojects {
    repositories {
        mavenCentral()
    }

    configure<BasePluginConvention> {
        archivesBaseName = "kormatter-$archivesBaseName"
    }

    dependencies {
    }
}
