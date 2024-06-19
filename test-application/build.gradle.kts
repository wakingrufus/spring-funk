plugins {
    application
    id("org.springframework.boot") version ("3.+")
    kotlin("jvm")
}
repositories {
    mavenCentral()
}
dependencies{
    implementation(project(":spring-funk-webmvc"))
    implementation("org.springframework.boot:spring-boot-starter-jetty:3.2.5")
    implementation("ch.qos.logback:logback-classic:1.5.6")

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}