plugins {
    kotlin("jvm") version "2.1.10"
    jacoco
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

tasks.jacocoTestReport {
    reports {
        csv.required.set(true)  // Enable CSV reports for additional processing if needed
        xml.required.set(true)  // Required for coverage-diff to work
        html.required.set(true) // Human-readable reports
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)

    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude("**/generated/**")
        }
    )
    sourceDirectories.setFrom(files("src/main/kotlin"))
    executionData.setFrom(fileTree(layout.buildDirectory).include("jacoco/test.exec"))

    violationRules {
        rule {
            limit {
                minimum = "1".toBigDecimal() // 100% coverage requirement
            }
        }
        rule {
            element = "CLASS"
            includes = listOf("org.moscow.*") // Adjust package name as needed

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "1".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "1".toBigDecimal()
            }
            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "1".toBigDecimal()
            }
        }
    }
}


tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn(tasks.test)

    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude("**/generated/**")
        }
    )
    sourceDirectories.setFrom(files("src/main/kotlin"))
    executionData.setFrom(fileTree(layout.buildDirectory).include("jacoco/test.exec"))
}