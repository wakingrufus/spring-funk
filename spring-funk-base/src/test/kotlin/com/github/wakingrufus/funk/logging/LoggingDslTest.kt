package com.github.wakingrufus.funk.logging

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.github.wakingrufus.funk.test.testDslApplication
import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.boot.logging.LoggingSystem

class LoggingDslTest {
    private val log = KotlinLogging.logger {}
    private lateinit var logAppender: ListAppender<ILoggingEvent>

    @BeforeEach
    fun setup() {
        logAppender = ListAppender<ILoggingEvent>()
        logAppender.start()
    }

    @AfterEach
    fun clean() {
        logAppender.stop()
    }

    @Test
    fun test() {
        val logger = LoggerFactory.getLogger(LoggingDslTest::class.java) as Logger
        logger.addAppender(logAppender)

        testDslApplication(LoggingDslInitializer()) {
            application {

            }
            environment {
            }
            test {
                log.info { "test" }
                assertThat(logAppender.list).hasSize(1)
                assertThat(logAppender.list[0].message).isEqualTo("test")
            }
        }
    }

    @Test
    fun `test root level`() {
        val logger = LoggerFactory.getLogger(LoggingDslTest::class.java) as Logger
        logger.addAppender(logAppender)

        testDslApplication(LoggingDslInitializer()) {
            application {
                logging {
                    root(LogLevel.DEBUG)
                }
            }
            environment {
            }
            test {
                val loggingSystem = LoggingSystem.get(LoggingDslTest::class.java.classLoader)
                assertThat(loggingSystem.getLoggerConfiguration("ROOT").effectiveLevel)
                    .isEqualTo(LogLevel.DEBUG)
            }
        }
    }

    @Test
    fun `test logger off`() {
        testDslApplication(LoggingDslInitializer()) {
            application {
                logging {
                    level<LoggingDslTest>(LogLevel.OFF)
                }
            }
            environment {
            }
            test {
                log.info { "test" }
                assertThat(logAppender.list).hasSize(0)
            }
        }
    }
}