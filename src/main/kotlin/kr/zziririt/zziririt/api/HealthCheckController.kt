package kr.zziririt.zziririt.api

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import kr.zziririt.zziririt.infra.mail.MailService
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

private val kLogger = KotlinLogging.logger {}

@RestController
class HealthCheckController(
    private val dataSource: DataSource,
    private val mailService: MailService
) {
    @GetMapping("/ping")
    fun ping() = "pong"

    @GetMapping("/tr-w")
    @Transactional
    @Throws(SQLException::class)
    fun primary() {
        val connection: Connection = dataSource.connection

        kLogger.info { "${"primary url : {}"} ${connection.metaData.url}" }
    }

    @GetMapping("/tr-ro")
    @Transactional(readOnly = true)
    @Throws(SQLException::class)
    fun secondary() {
        val connection: Connection = dataSource.connection

        kLogger.info { "${"secondary url : {}"} ${connection.metaData.url}" }
    }

    @GetMapping("/mail")
    fun sendMail(
        @RequestParam email: String,
    ) {
        runBlocking {
            mailService.sendMailToNewbie(email, "test", "test")
        }
    }
}