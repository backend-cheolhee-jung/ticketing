plugins {
    id("com.github.node-gradle.node") version "7.0.1"
}

node {
    download.set(true)
    version.set("18.0.0")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("buildTs") {
    dependsOn("npmInstall")
    args.set(listOf("run", "build"))
}

tasks.register("build") {
    dependsOn("buildTs")
}