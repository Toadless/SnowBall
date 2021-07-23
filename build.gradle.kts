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
    java
    idea
    application

    kotlin("jvm") version "1.5.20"
    id ("org.springframework.boot") version "2.4.2"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "net.toaddev"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven { setUrl ("http://repo.spring.io/plugins-release") }
    jcenter()
    maven { setUrl ("https://m2.dv8tion.net/releases") }
}

val jdaVersion = "4.3.0_277"
val logbackVersion = "1.3.0-alpha5"
val lavaplayerVersion = "1.3.78"
val yamlVersion = "1.7.2"
val groovyVersion = "3.0.7"
val mongoVersion = "3.12.8"
val jsonVersion = "20210307"
val caffeineVersion = "2.8.8"
val utilsVersion = "3.0.5"
val springBootVersion = "2.5.3"
val httpClientVersion = "4.9.1"
val spotifyVersion = "6.5.4"

dependencies {
    // spring boot
    implementation ("org.springframework.boot:spring-boot-starter")
    implementation ("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")

    // discord
    implementation("net.dv8tion:JDA:$jdaVersion")
    //implementation ("com.github.dv8fromtheworld:jda:feature~slash-commands-SNAPSHOT")
    implementation ("com.jagrosh:jda-utilities:$utilsVersion")

    // audio
    implementation ("com.github.sedmelluq:lavaplayer:$lavaplayerVersion")
    implementation ("se.michaelthelin.spotify:spotify-web-api-java:$spotifyVersion")

    // yaml config
    implementation ("me.carleslc.Simple-YAML:Simple-Yaml:$yamlVersion")
    // database
    implementation ("org.mongodb:mongo-java-driver:$mongoVersion")
    implementation ("org.json:json:$jsonVersion")

    // kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.5.0-native-mt")

    // other
    implementation ("io.github.classgraph:classgraph:4.8.110")
    implementation ("com.github.ben-manes.caffeine:caffeine:3.0.3")
    implementation ("com.squareup.okhttp3:okhttp:$httpClientVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}