plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.51"
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
}

taboolib {
    description {
        contributors {
            name("ItsFlicker")
        }
        dependencies {
            name("PlaceholderAPI").optional(true)
            name("Zaphkiel").optional(true)
            name("eco").optional(true)
        }
        load("STARTUP")
    }
    install("common", "common-5")
    install(
        "module-ai",
        "module-chat",
        "module-configuration",
        "module-nms",
        "module-nms-util",
        "module-kether",
        "module-metrics"
    )
    install("platform-bukkit")
    install("expansion-command-helper", "expansion-javascript")
    classifier = null
    version = "6.0.10-31"
}

repositories {
    mavenCentral()
//    mavenLocal()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.github.LoneDev6:api-itemsadder:3.2.5")
    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")
    compileOnly("com.willfp:eco:6.35.1")
    compileOnly("ink.ptms:Zaphkiel:2.0.14")

    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11903:11903:mapped")
    compileOnly("ink.ptms.core:v11903:11903:universal")

//    compileOnly("io.netty:netty-all:5.0.0.Alpha2")
//    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("com.electronwill.night-config:core:3.6.5")
    compileOnly("com.google.code.gson:gson:2.9.0")
    compileOnly("com.google.guava:guava:31.1-jre")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    taboo(fileTree("libs-shaded"))
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