plugins {
    java
    id("com.gradleup.shadow") version "8.3.0"
}

version = "0.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.17.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("org.tomlj:tomlj:1.1.1")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    withType<JavaCompile>() {
        options.compilerArgs.add("-Xlint:unchecked")
    }
    
    shadowJar {
        relocate("org.tomlj", "kr.choyunjin.chlorine.shadow.tomlj")
        relocate("org.antlr", "kr.choyunjin.chlorine.shadow.antlr")
        relocate("org.checkerframework", "kr.choyunjin.chlorine.shadow.checkerframework")
    }

    jar {
        finalizedBy(shadowJar)
    }
}