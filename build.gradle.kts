import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("io.izzel.taboolib") version "2.0.23"
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
}

taboolib {
    description {
        contributors {
            name("ItsFlicker")
        }
        dependencies {
            name("CraftEngine").optional(true)
            name("eco").optional(true)
            name("ItemsAdder").optional(true)
            name("Oraxen").optional(true)
            name("PlaceholderAPI").optional(true)
            name("Sandalphon").optional(true)
            name("Zaphkiel").optional(true)
        }
        load("STARTUP")
    }
    env {
        install("basic-configuration")
        install(BukkitHook, BukkitUtil, BukkitNMSEntityAI)
        install(
            "bukkit-nms-legacy",
            "bukkit-nms-stable",
            "bukkit-nms-tag",
            "bukkit-nms-tag-12005",
            "bukkit-nms-tag-12105",
            "bukkit-nms-tag-12106",
            "bukkit-nms-tag-legacy",
            "bukkit-nms"
        )
        install(
            "minecraft-chat",
            "minecraft-command-helper",
//            "minecraft-i18n",
            "minecraft-kether",
            "minecraft-metrics"
        )
        install(JavaScript)
        install(Bukkit)
//        repoTabooLib = "http://mcitd.cn:8081/repository/releases"
    }
    version {
        taboolib = "6.2.3-7105e58f"
        coroutines = null
    }
}

repositories {
    mavenLocal()
    mavenCentral()
//    maven("http://mcitd.cn:8081/repository/releases") { isAllowInsecureProtocol = true }
    maven("https://jitpack.io")
    maven("https://repo.momirealms.net/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
//    compileOnly("public:ModelEngine:3.0.0")
    compileOnly("com.willfp:eco:6.35.1")
    compileOnly("com.github.LoneDev6:api-itemsadder:3.2.5")
    compileOnly("net.momirealms:craft-engine-core:0.0.59")
    compileOnly("net.momirealms:craft-engine-bukkit:0.0.59")
//    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")
    compileOnly("ink.ptms:Zaphkiel:2.0.14")

    compileOnly("ink.ptms.core:v12107:12107:mapped")
    compileOnly("ink.ptms.core:v12107:12107:universal")
//    compileOnly("ink.ptms:nms-all:1.0.0")

//    compileOnly("com.electronwill.night-config:core:3.6.7")
    compileOnly("com.google.code.gson:gson:2.9.0")
    compileOnly("com.google.guava:guava:31.1-jre")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    taboo(fileTree("libs-shaded"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}