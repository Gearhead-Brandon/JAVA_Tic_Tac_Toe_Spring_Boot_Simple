import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("java")
    id("jacoco")
}

group = "game.tictactoe"
version = "1.0-SNAPSHOT"

var mainClassName = "game.tictactoe.application.TicTacToeApplication"
var lombokVersion = "1.18.36"
var mapstructVersion = "1.6.3"
var junitJupiterVersion = "5.10.0"
var openapiVersion = "2.7.0"
var jsonSchemaValidatorVersion = "2.2.14"
var assertjVersion = "3.27.2"
var jacocoExclude = listOf(
    "game/tictactoe/application/TicTacToeApplication.class",
    "game/tictactoe/configuration/CorsConfig.class",
    "game/tictactoe/configuration/SwaggerConfig.class"
)

java{
    sourceCompatibility = JavaVersion.VERSION_23
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    developmentOnly {
        extendsFrom(configurations.implementation.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$openapiVersion")

    implementation("com.github.java-json-tools:json-schema-validator:$jsonSchemaValidatorVersion")

    compileOnly("org.mapstruct:mapstruct:$mapstructVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    testImplementation(platform("org.junit:junit-bom:$junitJupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.compileJava.configure {
    options.compilerArgs.add("-parameters")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = mainClassName
        attributes["Class-Path"] = configurations.compileClasspath.get().joinToString(" ")
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }

    classDirectories.setFrom(classDirectories.files.map {
        fileTree(it).matching {
            exclude(jacocoExclude)
        }
    })
}