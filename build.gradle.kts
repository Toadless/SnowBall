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
val lavaplayerVersion = "1.3.66"
val yamlVersion = "1.7.2"
val groovyVersion = "3.0.7"
val mongoVersion = "3.12.7"
val jsonVersion = "20180813"

dependencies {
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation ("com.sedmelluq:lavaplayer:$lavaplayerVersion")
    implementation ("me.carleslc.Simple-YAML:Simple-Yaml:$yamlVersion")
    implementation ("org.codehaus.groovy:groovy-jsr223:$groovyVersion")
    implementation ("org.mongodb:mongo-java-driver:$mongoVersion")
    implementation ("org.json:json:$jsonVersion")
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