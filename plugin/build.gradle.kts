import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.0"
    java
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

version = "0.0.1"

repositories {
    mavenCentral()
    maven(url="https://mvn.lumine.io/repository/maven-public/")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    implementation("com.oop.orangeengine:file:5.0")
    implementation("com.oop.orangeengine:item:5.0")
    implementation("com.oop.orangeengine:engine:5.0")
    implementation("com.oop.orangeengine:particle:5.0")
    implementation("com.oop.orangeengine:inventory:5.0")
    implementation("com.oop.orangeengine:command:5.0")
    implementation("com.oop.orangeengine:eventssubscription:5.0")
    implementation("com.oop.orangeengine:message:5.0")
    implementation(project(":api"))
    implementation(fileTree("../lib"))

    implementation("org.codemc.worldguardwrapper:worldguardwrapper:1.2.0-SNAPSHOT")
    compileOnly("io.lumine.xikage:MythicMobs:4.10.0-SNAPSHOT")
}

tasks {
    named<ShadowJar>("shadowJar") {
        destinationDirectory.set(file("target"))
        archiveFileName.set("BossesExpansion.jar")

        relocate("com.oop.orangengine", "com.honeybeedev.bossesexpansion.engine")
        relocate("com.oop.datamodule", "com.honeybeedev.bossesexpansion.datamodule")
    }

    withType<ProcessResources> {
        eachFile {
            if (this.name.contentEquals("plugin.yml"))
                filter { it.replace("{project.version}", version.toString()) }
        }
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}