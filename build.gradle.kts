plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "xyz.jungha.buildingblock"
version = "1.0.1"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.nexomc.com/releases")
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    compileOnly("com.nexomc:nexo:1.8.0")

    compileOnly(fileTree("libs") { include("*.jar") })

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.release = 21
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        archiveFileName.set("BuildingBlock.jar")
        destinationDirectory.set(File("C:\\Users\\sjh05\\Documents\\서버\\1.21.4\\plugins"))
    }

    build {
        dependsOn(shadowJar)
    }
}
