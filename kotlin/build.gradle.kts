plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "com.github.davidkleiven"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.jena:jena:5.1.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "com.github.davidkleiven.pyjena.MainKt"
}
