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
        active = Active.ALWAYS
        armored = true
        publicKey = System.getenv("PUBLIC_KEY")
        secretKey = System.getenv("PRIVATE_KEY")
        passphrase = System.getenv("PASSPHRASE")
    }
    release {
        github {
            repoOwner = "wakingrufus"
            host = "github.com"
            username = "wakingrufus"
            apiEndpoint = "https://api.github.com"
            token = System.getenv("GITHUB_TOKEN")
            enabled = true
            releaseNotes {
                enabled = true
            }
            changelog {
                enabled = false
            }
            skipTag = true
            sign = false
        }
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                    applyMavenCentralRules = true
                    username = System.getenv("SONATYPE_USER")
                    password = System.getenv("SONATYPE_PASS")
                    namespace.set("io.github.wakingrufus")
                }
            }
        }
    }
}
project.tasks.named("jreleaserFullRelease") {
    dependsOn(subprojects.map { it.tasks.named("publish") })
}
tasks.create<Delete>("clean") {
    setDelete(layout.buildDirectory)
}