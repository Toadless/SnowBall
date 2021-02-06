plugins {
    application
    id ("org.springframework.boot") version "2.4.2"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "net.toaddev"
version = "4.0.0"

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
    maven { setUrl ("http://repo.spring.io/plugins-release") }
}

val jdaVersion = "4.2.0_223"
val logbackVersion = "1.3.0-alpha5"
val lavaplayerVersion = "1.3.67"
val yamlVersion = "1.7.2"
val groovyVersion = "3.0.7"
val mongoVersion = "3.12.7"
val jsonVersion = "20180813"
val caffeineVersion = "2.8.8"
val utilsVersion = "3.0.5"
val springBootVersion = "2.1.8.RELEASE"

dependencies {
    // spring boot
    implementation ("org.springframework.boot:spring-boot-starter")
    implementation ("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")

    // discord
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation ("com.jagrosh:jda-utilities:$utilsVersion")

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
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}