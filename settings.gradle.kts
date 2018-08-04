include("platform:js", "platform:jvm", "platform:common")
rootProject.name = "kormatter"

val nodejs_plugin_version: String by settings

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if(requested.id.id == "com.moowork.node")
                useVersion(nodejs_plugin_version)
        }
    }
}