import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "net.toaddev"
version = "4.0.0"

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

val jdaVersion = "4.2.0_223"
val logbackVersion = "1.3.0-alpha5"
val lavaplayerVersion = "1.3.67"
val yamlVersion = "1.7.2"
val groovyVersion = "3.0.7"
val mongoVersion = "3.12.7"
val jsonVersion = "20180813"
val caffeineVersion = "2.8.8"

dependencies {
    // logback
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // discord
    implementation("net.dv8tion:JDA:$jdaVersion")

    // audio
    implementation ("com.sedmelluq:lavaplayer:$lavaplayerVersion")

    // yaml config
    implementation ("me.carleslc.Simple-YAML:Simple-Yaml:$yamlVersion")

    //eval
    implementation ("org.codehaus.groovy:groovy-jsr223:$groovyVersion")

    // database
    implementation ("org.mongodb:mongo-java-driver:$mongoVersion")
    implementation ("org.json:json:$jsonVersion")

    // other
    implementation ("io.github.classgraph:classgraph:4.8.98")
    implementation ("com.github.ben-manes.caffeine:caffeine:2.8.8")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_10
    targetCompatibility = JavaVersion.VERSION_1_10
}

application {
    mainClassName = "net.toaddev.lavalite.main.Launcher"
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("app")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to application.mainClassName))
        }
    }
}