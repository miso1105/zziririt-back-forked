package kr.zziririt.zziririt.global

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

private val kLogger = KotlinLogging.logger {}
@Component
class AppStatusRunner(private val environment: Environment) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        kLogger.info { "===================다중 프로파일 테스트===================" }
        kLogger.info { "Active profiles : ${environment.activeProfiles.toList()}" }
        kLogger.info { "Datasource driver : ${environment.getProperty("spring.datasource.driver-class-name")}" }
        kLogger.info { "Datasource url : ${environment.getProperty("spring.datasource.url")}" }
        kLogger.info { "Datasource username : ${environment.getProperty("spring.datasource.username")}" }
        kLogger.info { "Datasource password : ${environment.getProperty("spring.datasource.password")}" }
        kLogger.info { "Redis Host : ${environment.getProperty("spring.data.redis.host")}" }
        kLogger.info { "Redis Port : ${environment.getProperty("spring.data.redis.port")}" }
        kLogger.info { "Server Port : ${environment.getProperty("server.port")}" }
        kLogger.info { "Default Property : ${environment.getProperty("default.string")}" }
        kLogger.info { "Common Property : ${environment.getProperty("common.string")}" }
        kLogger.info { "====================================================" }
    }
}