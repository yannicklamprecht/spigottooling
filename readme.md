# Spigot Plugin Tooling

A Gradle plugin that automatically downloads BuildTools and SpecialSource, allows per submodule spigot configuration, builds specific versions and remaps version dependent code developed against Mojang mappings to Spigot mappings using SpecialSource.


## Usage

settings.gradle(.kts)
```kotlin
pluginManagement {
    repositories {
        maven {
            name = "eldonexus"
            // url = uri("https://eldonexus.de/repository/maven-snapshots/")
            url = uri("https://eldonexus.de/repository/maven-releases/")
        }
    }
}
```

build.gradle(.kts)

```kotlin
plugins {
    id("com.github.yannicklamprecht.spigot.tools") version "1.0.0"
}
```

```kotlin
tasks {
    spigotTools {
        mojangMapped.set(true)
        version.set("1.17")
        spigotVersion.set("1.17-R0.1-SNAPSHOT")
        outputClassifier.set("spigot-mapped")
    }   
}
```

````kotlin
dependencies {
    compileOnly("org.spigotmc:spigot:1.17-R0.1-SNAPSHOT:remapped-mojang")
}
````
