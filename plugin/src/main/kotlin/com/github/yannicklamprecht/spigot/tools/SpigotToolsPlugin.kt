package com.github.yannicklamprecht.spigot.tools

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import java.net.URL
import java.nio.file.Path


class SpigotToolsPlugin : Plugin<Project> {

    private fun download(targetPath: Path, url: URL) {
        if (!targetPath.toFile().exists()) {
            targetPath.parent.toFile().mkdirs()
            targetPath.toFile().writeBytes(url.readBytes())
        }
    }

    override fun apply(project: Project) {

        val extension: SpigotToolsExtension = project.extensions.create(
            "spigotTools",
            SpigotToolsExtension::class.java
        )

        project.tasks.register("buildSpigot", BuildSpigotTask::class.java) {
            it.group = taskGroup
            it.description = "Builds Spigot"
            it.version.set(extension.version)
            it.mojangMapped.set(extension.mojangMapped)
        }

        project.tasks.register("remap", RemapJar::class.java) {
            it.group = taskGroup
            it.description = "Remaps the artifact from Mojang Mappings to Spigot mapping."
            it.outputClassifier.set(extension.outputClassifier)
            it.spigotVersion.set(extension.spigotVersion)
            it.dependsOn(project.tasks.withType(Jar::class.java))
            it.mojangMapped.set(extension.mojangMapped)
        }
        project.tasks.withType(Jar::class.java) {
            it.finalizedBy(project.tasks.withType(RemapJar::class.java))
        }


        project.tasks.register("setup") {
            it.group = taskGroup
            it.description = "Setups the tools used: SpecialSource, BuildTools"
            it.doFirst {
                tooling.toFile().mkdirs()
                download(buildToolsJar, buildToolsUrl)
                download(specialsourePath, specialsourceUrl)
            }
            it.finalizedBy(project.tasks.withType(BuildSpigotTask::class.java))
        }

        project.tasks.register("cleanup") {
            it.group = taskGroup
            it.description = "Cleanup the tools used: SpecialSource, BuildTools"
            it.doLast {
                tooling.toFile().deleteRecursively()
            }
        }
    }

}
