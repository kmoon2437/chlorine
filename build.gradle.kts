plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
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
    
    // implementation으로 설정한 것들을 jar에다가 집어넣어줌
    shadowJar {
        relocate("org.tomlj", "kr.choyunjin.chlorine.shadow.tomlj")
        relocate("org.antlr", "kr.choyunjin.chlorine.shadow.antlr")
        relocate("org.checkerframework", "kr.choyunjin.chlorine.shadow.checkerframework")
    }

    jar {
        finalizedBy(shadowJar)
    }

    runServer {
        minecraftVersion("1.21.1")
    }
}