plugins {
    kotlin("jvm") version ("2.0.0") apply (false)
    id("jacoco-report-aggregation")
}

tasks.wrapper {
    gradleVersion = "8.7"
    distributionType = Wrapper.DistributionType.BIN
}
 
subprojects {
    group = "com.github.wakingrufus.springdsl"
}

dependencies {
    subprojects.forEach {
        jacocoAggregation(project(":"+it.name))
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

//tasks.named("build") {
//    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
//}