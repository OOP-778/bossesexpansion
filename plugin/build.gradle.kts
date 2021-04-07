import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.json.JsonSlurper

plugins {
    kotlin("jvm") version "1.4.32"
    java
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

val jsonFile = File("version.json")
val obj = JsonSlurper().parse(jsonFile) as Map<*, *>

version = obj["version"]!!

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    implementation("com.oop.orangeengine:file:5.3")
    implementation("com.oop.orangeengine:item:5.3")
    implementation("com.oop.orangeengine:engine:5.3")
    implementation("com.oop.orangeengine:particle:5.3")
    implementation("com.oop.orangeengine:inventory:5.3")
    implementation("com.oop.orangeengine:command:5.3")
    implementation("com.oop.orangeengine:eventssubscription:5.3")
    implementation("com.oop.orangeengine:message:5.3")
    implementation(project(":api"))
    compileOnly(fileTree("../lib"))

    implementation("com.oop.inteliframework:scoreboard:1.0")
    implementation("com.oop.inteliframework:dependency-common:1.0")

    implementation("org.codemc.worldguardwrapper:worldguardwrapper:1.2.0-SNAPSHOT")
    compileOnly("io.lumine.xikage:MythicMobs:4.10.0-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
}

tasks {
    named<ShadowJar>("shadowJar") {
        destinationDirectory.set(file("/run/media/oop-778/BRABARAR/Serrvers/OOP/BossesExpansionTest/plugins/"))
        archiveFileName.set("BossesExpansion.jar")

        relocate("com.oop.orangengine", "org.royalix.bossesexpansion.engine")
        relocate("com.oop.datamodule", "org.royalix.bossesexpansion.datamodule")
        relocate("com.inteliframework", "org.royalix.if")
        relocate("kotlin", "org.royalix.bossesexpansion.lib.kotlin")
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
