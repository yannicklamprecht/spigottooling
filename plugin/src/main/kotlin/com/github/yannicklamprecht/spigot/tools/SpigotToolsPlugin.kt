package com.github.yannicklamprecht.spigot.tools

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
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

        project.tasks.register("buildSpigot", BuildSpigotTask::class.java) {
            it.group = taskGroup
            it.description = "Builds Spigot"
            it.version.set(extension.version)
            it.mojangMapped.set(extension.mojangMapped)
        }


        project.tasks.whenTaskAdded {
            if(it.name == "jar" && it.enabled){
                val remapTask = project.tasks.register("remap", RemapJar::class.java) { remapJar ->
                    // remapJar.group = taskGroup
                    remapJar.description = "Remaps the artifact from Mojang Mappings to Spigot mapping."
                    remapJar.outputClassifier.set(extension.outputClassifier)
                    remapJar.inputTask.set(project.tasks.getByName("jar"))
                    remapJar.spigotVersion.set(extension.spigotVersion())

                    remapJar.mojangMapped.set(extension.mojangMapped)
                    remapJar.dependsOn(project.tasks.withType(Jar::class.java))
                    project.tasks.named("build").get().dependsOn(remapJar)
                }

                project.plugins.withId("com.github.johnrengelman.shadow") { shadowPlugin ->

                    project.tasks.whenTaskAdded { task ->
                        if(task.name == "shadowJar" && task.enabled){
                            remapTask.get().inputTask.set(it)
                            remapTask.get().setDependsOn(listOf(it))
                            remapTask.get().finalizedBy(task)
                            project.tasks.register("shadowJarSpigot") { shadowJarSpigot ->
                                shadowJarSpigot.group = taskGroup
                                shadowJarSpigot.description = "ShadowJar but with remapping artifacts before."
                                shadowJarSpigot.dependsOn(project.tasks.withType(RemapJar::class.java))
                            }

                            project.tasks.register("shadowJarMojang") { shadowJarMojang ->
                                shadowJarMojang.group = taskGroup
                                shadowJarMojang.description = "Simply ShadowJar"
                            }
                        }
                    }
                }

            }
        }


    }

}
