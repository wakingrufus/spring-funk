import org.jreleaser.model.Active

plugins {
    kotlin("jvm") version ("2.0.0") apply (false)
    id("jacoco-report-aggregation")
    id("org.jreleaser") version ("1.13.1")
}

tasks.wrapper {
    gradleVersion = "8.7"
    distributionType = Wrapper.DistributionType.BIN
}

allprojects {
    group = "io.github.wakingrufus"
}

dependencies {
    subprojects.forEach {
        jacocoAggregation(project(":" + it.name))
    }
}
repositories {
    mavenCentral()
}

reporting {
    reports {
        val testCodeCoverageReport by creating(JacocoCoverageReport::class) {
            testType = TestSuiteType.UNIT_TEST
        }
    }
}

jreleaser {
    signing {
        active.set(Active.ALWAYS)
        armored = true
        publicKey.set(System.getenv("PUBLIC_KEY"))
        secretKey.set(System.getenv("PRIVATE_KEY"))
        passphrase.set(System.getenv("PASSPHRASE"))
    }
    release {
        github {
            token.set(System.getenv("GITHUB_TOKEN"))
            enabled.set(false)
        }
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(Active.ALWAYS)
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                    applyMavenCentralRules.set(true)
                    username.set(System.getenv("SONATYPE_USER"))
                    password.set(System.getenv("SONATYPE_PASS"))
                    namespace.set("io.github.wakingrufus")
                }
            }
        }
    }
}
project.tasks.named("jreleaserAssemble") {
    dependsOn(subprojects.map { it.tasks.named("publish") })
}
tasks.create<Delete>("clean") {
    setDelete(layout.buildDirectory)
}