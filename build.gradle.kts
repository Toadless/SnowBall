/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

plugins {
    application
    id ("org.springframework.boot") version "2.4.2"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "net.toaddev"
version = "4.1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
    maven { setUrl ("http://repo.spring.io/plugins-release") }
}

val jdaVersion = "4.2.0_228"
val logbackVersion = "1.3.0-alpha5"
val lavaplayerVersion = "1.3.67"
val yamlVersion = "1.7.2"
val groovyVersion = "3.0.7"
val mongoVersion = "3.12.7"
val jsonVersion = "20201115"
val caffeineVersion = "2.8.8"
val utilsVersion = "3.0.5"
val springBootVersion = "2.1.8.RELEASE"
val httpClientVersion = "4.9.1"
val spotifyVersion = "6.3.0"

dependencies {
    // spring boot
    implementation ("org.springframework.boot:spring-boot-starter")
    implementation ("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")

    // discord
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation ("com.jagrosh:jda-utilities:$utilsVersion")

    // audio
    implementation ("com.sedmelluq:lavaplayer:$lavaplayerVersion")
    implementation ("se.michaelthelin.spotify:spotify-web-api-java:$spotifyVersion")

    // yaml config
    implementation ("me.carleslc.Simple-YAML:Simple-Yaml:$yamlVersion")

    //eval
    implementation ("org.codehaus.groovy:groovy-jsr223:$groovyVersion")

    // database
    implementation ("org.mongodb:mongo-java-driver:$mongoVersion")
    implementation ("org.json:json:$jsonVersion")

    // other
    implementation ("io.github.classgraph:classgraph:4.8.102")
    implementation ("com.github.ben-manes.caffeine:caffeine:2.8.8")
    implementation ("com.squareup.okhttp3:okhttp:$httpClientVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}