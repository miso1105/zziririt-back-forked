package kr.zziririt.zziririt.infra.mail

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import kr.zziririt.zziririt.infra.aws.ses.AwsSesClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

private val kLogger = KotlinLogging.logger {}

@Service
class MailService(
    private val awsSesClient: AwsSesClient,
    @Value("\${cloud.aws.ses.sender-email}") private val senderEmail: String,
) {
    fun sendMailToNewbie(recipient: String, nickname: String, createdAt: String) {
        val sender = senderEmail
        val subject = "Zziririt 가입 환영 이메일"
        val bodyHTML = (
                "<!DOCTYPE html>\n" +
                        "<html xmlns:th=\"http://www.thymeleaf.org\" lang=\"ko-kr\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid #2A7AF3; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">\n" +
                        "    <img src=\"https://zziririt.kr/zziririt-logo.png\" width=\"250\">\n" +
                        "    <h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">\n" +
                        "        <span style=\"color: #2A7AF3\">가입 정보</span> 안내입니다.\n" +
                        "    </h1>\n" +
                        "    <div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid #2A7AF3; margin: 30px auto; padding: 30px 0; box-sizing: border-box;\">\n" +
                        "        <p style=\"font-size: 16px; line-height: 26px; margin-top: 20px; padding: 0 5px;\">\n" +
                        "            <b style=\"color: #2A7AF3\">닉네임</b>: <span >${nickname}</span></p>\n" +
                        "        <p style=\"font-size: 16px; line-height: 26px; margin-top: 20px; padding: 0 5px;\">\n" +
                        "            <b style=\"color: #2A7AF3\">가입일</b>: <span >${createdAt}</span></p>\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>"
                )

        try {
            runBlocking {
                awsSesClient.send(sender, recipient, subject, bodyHTML)
            }
        } catch (e: Exception) {
            //TODO : 추후 메일 처리 실패시 기록하여 관리할 수 있도록 수정.
            kLogger.error { e.message }
            e.printStackTrace()
        }
    }
}