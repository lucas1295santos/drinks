import org.jetbrains.kotlin.cli.jvm.compiler.findMainClass
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    application
}

group = "me.lucas"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

val ktorVersion = "1.1.4"

dependencies {
    //ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    //jackson is a library for transforming data to objects and vice-versa
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    //exposed provides some abstractions to work with databases
    implementation("org.jetbrains.exposed:exposed:0.15.1")
    //postgres is the database used in the project
    implementation("org.postgresql:postgresql:42.2.5")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}