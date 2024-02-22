import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java-library")
}

version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "java-library")

    group = "uk.protonull.test"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
        testLogging.let {
            it.events(*TestLogEvent.values())
            it.exceptionFormat = TestExceptionFormat.FULL
            it.showCauses = true
            it.showExceptions = true
            it.showStackTraces = true
        }
    }
}
