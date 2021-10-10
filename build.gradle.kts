import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

group = "me.lucas"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

application {
    mainClass.set("MainKt")
}

val ktorVersion = "1.6.1"

dependencies {
    //ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    //jackson is a library for transforming data to objects and vice-versa
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    //exposed provides some abstractions to work with databases
    implementation("org.jetbrains.exposed:exposed-core:0.34.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.34.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.34.1")
    //postgres is the database used in the project
    implementation("org.postgresql:postgresql:42.2.5")
    //kodein is a DI tool
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:7.0.0")

    testImplementation(kotlin("test"))
}

// Creates an executable jar for the application, pointing Main.kt as the class that contains the main method
tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "MainKt"))
        }
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}