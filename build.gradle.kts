plugins {
    kotlin("jvm") version ("2.0.0") apply (false)
}

tasks.wrapper {
    gradleVersion = "8.7"
    distributionType = Wrapper.DistributionType.BIN
}
 
subprojects {
    group = "com.github.wakingrufus.springdsl"
}