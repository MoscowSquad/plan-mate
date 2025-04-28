import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.10"
    jacoco
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

// Kotlin version verification task
tasks.register("kotlinVersionCheck") {
    doLast {
        require(kotlin.coreLibrariesVersion == "2.1.10") {
            "Kotlin version mismatch! Expected 2.1.10, found ${kotlin.coreLibrariesVersion}"
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory.set(layout.buildDirectory.dir("reports/jacoco"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(
                    "**/config/**",
                    "**/*Application*",
                    "**/model/**"
                )
            }
        })
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

tasks.build {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

group = "org.moscow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("io.arrow-kt:arrow-core:2.0.1")
    implementation("io.arrow-kt:arrow-fx-coroutines:2.0.1")
    implementation ("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0-M1")
    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation("com.google.truth:truth:1.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}