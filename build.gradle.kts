plugins {
    kotlin("jvm") version "2.1.10"
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