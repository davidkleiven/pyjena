plugins {
    kotlin("jvm") version "2.0.0"
    application
    id("com.gradleup.shadow") version "8.3.2"
}

group = "com.github.davidkleiven"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.jena:jena-tdb2:5.1.0")
    implementation("net.sf.py4j:py4j:0.10.9.7")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "com.github.davidkleiven.pyjena.MainKt"
}

tasks.shadowJar {
    manifest.attributes["Main-Class"] = application.mainClass
    archiveFileName = "pyjena-service.jar"
}

tasks.register<Copy>("copy-jar-to-python") {
    dependsOn("shadowJar")
    from("build/libs/pyjena-service.jar")
    into("../python/src/pyjena/backend")
}
