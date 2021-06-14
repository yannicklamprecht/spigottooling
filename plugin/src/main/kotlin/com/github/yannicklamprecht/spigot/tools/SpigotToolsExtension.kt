package com.github.yannicklamprecht.spigot.tools

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class SpigotToolsExtension {

    @get:Input
    abstract val version: Property<String>

    @get:Input
    abstract val mojangMapped: Property<Boolean>

    @get:Input
    abstract val spigotVersion: Property<String>

    @get:Input
    @get:Optional
    abstract val inputClassifier: Property<String>

    @get:Input
    @get:Optional
    abstract val outputClassifier: Property<String>
}
