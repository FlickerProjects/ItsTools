plugins {
    `java-library`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

taboolib {
    description {
        contributors {
            name("ItsFlicker")
        }
        dependencies {
            name("eco").optional(true)
            name("ItemsAdder").optional(true)
            name("Oraxen").optional(true)
            name("PlaceholderAPI").optional(true)
            name("Sandalphon").optional(true)
            name("Zaphkiel").optional(true)
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
    version = "6.0.12-local"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
//    compileOnly("public:ModelEngine:3.0.0")
    compileOnly("com.willfp:eco:6.35.1")
    compileOnly("com.github.LoneDev6:api-itemsadder:3.2.5")
//    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")
    compileOnly("ink.ptms:Zaphkiel:2.0.14")

    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly("ink.ptms:nms-all:1.0.0")

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