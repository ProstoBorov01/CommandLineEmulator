plugins {
    application
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.program.Main")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "org.program.Main"
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-compress:1.21")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks.test {
    useJUnitPlatform()
}