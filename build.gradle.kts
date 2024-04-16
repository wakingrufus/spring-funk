plugins {
    kotlin("jvm") version ("1.9.22") apply (false)
}
tasks.wrapper {
    gradleVersion = "8.6"
    distributionType = Wrapper.DistributionType.BIN
}
 
subprojects {
    group = "com.github.wakingrufus"
}