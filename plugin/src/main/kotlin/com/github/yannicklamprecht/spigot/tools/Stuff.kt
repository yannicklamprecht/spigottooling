package com.github.yannicklamprecht.spigot.tools

import java.net.URL
import java.nio.file.Path


const val taskGroup = "spigottools"

val tooling: Path = Path.of("tooling")

val buildToolsPath: Path = tooling.resolve(Path.of("buildtools"))
val buildToolsJar: Path = buildToolsPath.resolve(Path.of("BuildTools.jar"))
val specialsourePath: Path = tooling.resolve(Path.of("specialsource", "SpecialSource.jar"))

val buildToolsUrl =
    URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar")
val specialsourceUrl =
    URL("https://repo.maven.apache.org/maven2/net/md-5/SpecialSource/1.10.0/SpecialSource-1.10.0-shaded.jar")

