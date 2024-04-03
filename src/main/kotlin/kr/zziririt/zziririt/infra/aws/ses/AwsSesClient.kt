package kr.zziririt.zziririt.infra.aws.ses

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.ses.SesClient
import aws.sdk.kotlin.services.ses.model.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val kLogger = KotlinLogging.logger {}

@Component
class AwsSesClient(
    @Value("\${cloud.aws.credentials.accessKey}") private val accessKey: String,
    @Value("\${cloud.aws.credentials.secretKey}") private val secretKey: String,
    @Value("\${cloud.aws.ses.sender-email}") private val senderEmail: String,
    @Value("\${cloud.aws.region.static}") private var regionStr: String
): MailClient {

    override suspend fun send(
        sender: String?,
        recipient: String,
        subjectVal: String?,
        bodyHTML: String?
    ): SendEmailResponse {

        val destinationOb = Destination {
            toAddresses = listOf(recipient)
        }

        val contentOb = Content {
            data = bodyHTML
        }

        val subOb = Content {
            data = subjectVal
        }

        val bodyOb = Body {
            html = contentOb
        }

        val msgOb = Message {
            subject = subOb
            body = bodyOb
        }

        val emailRequest = SendEmailRequest {
            destination = destinationOb
            message = msgOb
            source = sender
        }

        SesClient {
            region = regionStr
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = accessKey
                secretAccessKey = secretKey
            }
        }.use { sesClient ->
            kLogger.info { "Amazon SES에 AWS SDK for Kotlin을 이용하여 메일 전송 시도중 ..." }
            return sesClient.sendEmail(emailRequest)
        }
    }
}