plugins {
    kotlin("jvm") version "2.0.20"
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
    implementation("org.json:json:20240303")
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    // implementation으로 설정한 것들을 jar에다가 집어넣어줌
    shadowJar {
        dependencies {
            // kotlin 관련된 부분들은 jar에서 제외
            // (plugin.yml에 따로 적어 놓았음)
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            exclude(dependency("org.jetbrains:annotations"))
        }

        relocate("org.json", "kr.choyunjin.chlorine.shadow.org.json")
    }

    jar {
        finalizedBy(shadowJar)
    }

    runServer {
        minecraftVersion("1.21.1")
    }
}