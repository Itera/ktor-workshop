plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.detekt)
    alias(libs.plugins.versions)
    alias(libs.plugins.serialization)
    alias(libs.plugins.dependency.analysis)
    jacoco
}

group = "com.itera"
version = "0.0.1"

application {
    mainClass.set("com.itera.ApplicationKt")
}

kotlin {
    jvmToolchain(22)

    compilerOptions {
        freeCompilerArgs = listOf("-Xconsistent-data-class-copy-visibility")
    }
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.auth)
    implementation(libs.bundles.serialization)
    implementation(libs.bundles.monitoring)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.kotlin.test.junit)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}
