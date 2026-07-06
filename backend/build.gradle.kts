plugins {
    kotlin("jvm")
    application
}

group = "com.example.proyectoguardia"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:3.0.0")
    implementation("io.ktor:ktor-server-netty:3.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")
    implementation("io.ktor:ktor-server-cors:3.0.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

application {
    mainClass.set("com.example.proyectoguardia.ApplicationKt")
}

// No usamos toolchain explícito para evitar el error de aprovisionamiento en entornos restringidos
// Gradle usará el JDK por defecto del sistema/IDE

// Tarea para construir un Fat JAR
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    
    manifest {
        attributes["Main-Class"] = "com.example.proyectoguardia.ApplicationKt"
    }
    
    from(sourceSets.main.get().output)
    
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
