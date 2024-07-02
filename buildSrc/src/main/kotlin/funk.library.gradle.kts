plugins {
    `java-library`
    `jvm-test-suite`
    jacoco
    id("jacoco-report-aggregation")
    `maven-publish`
  //  id("org.jreleaser")
}
repositories {
    mavenCentral()
}
project.tasks.named("build") {
    dependsOn("jacocoTestReport")
}
testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}
java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name

            from(components["java"])

            pom {
                name = project.name
                description = "Sample application"
                url = "https://github.com/wakingrufus/spring-funk"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://spdx.org/licenses/Apache-2.0.html"
                    }
                }
                developers {
                    developer {
                        id = "wakingrufus"
                        name = "John Burns"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/wakingrufus/spring-funk.git"
                    developerConnection = "scm:git:ssh://github.com/wakingrufus/spring-funk.git"
                    url = "http://github.com/wakingrufus/spring-funk"
                }
            }
        }
    }

    repositories {
        maven {
            url = rootProject.layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}
