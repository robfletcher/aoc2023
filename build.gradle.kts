import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.9.21"
  id("com.bnorm.power.kotlin-power-assert") version "0.13.0"
  kotlin("plugin.serialization") version "1.9.21"
  application
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    languageVersion = "1.9"
  }
}

configure<com.bnorm.power.PowerAssertGradleExtension> {
  functions = listOf("kotlin.assert", "kotlin.check", "kotlin.require")
}

application {
  mainClass.set("MainKt")
  applicationDefaultJvmArgs = listOf("-ea")
}
