plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    description {
        contributors {
            name("ItsFlicker")
        }
        dependencies {
            name("Zaphkiel").optional(true)
            name("eco").optional(true)
        }
        load("STARTUP")
    }
    install("common", "common-5")
    install("module-ai")
    install("module-chat")
    install("module-configuration")
    install("module-nms", "module-nms-util")
    install("module-kether")
    install("module-metrics")
    install("platform-bukkit")
    install("expansion-command-helper")
    install("expansion-javascript")
    classifier = null
    version = "6.0.9-81"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.github.oraxen:oraxen:master-SNAPSHOT")
    compileOnly("com.willfp:eco:6.35.1")
    compileOnly("ink.ptms:Zaphkiel:1.7.2")

    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11900:11900:mapped")
    compileOnly("ink.ptms.core:v11900:11900:universal")

    compileOnly("com.google.code.gson:gson:2.8.5")
    compileOnly("com.google.guava:guava:21.0")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}