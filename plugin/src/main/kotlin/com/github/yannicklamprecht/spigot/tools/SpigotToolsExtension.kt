package com.github.yannicklamprecht.spigot.tools

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class SpigotToolsExtension {

    @get:Input
    abstract val version: Property<String>

    @get:Input
    abstract val mojangMapped: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val classifier: Property<String>

    fun spigotVersion(): Provider<String> {
        return version.zip(classifier.orElse("R0.1-SNAPSHOT"), this::map)
    }

    private fun map(a: String, b: String): String {
        return "${a}-${b}"
    }


    @get:Input
    @get:Optional
    abstract val outputClassifier: Property<String>
}
