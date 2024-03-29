package kr.zziririt.zziririt.infra.mailsender

import jakarta.mail.Message
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class MailService(
    private val javaMailSender: JavaMailSender
) : MailClient {

    val message = javaMailSender.createMimeMessage()
    val zziririt = "cs@zziririt.kr"

    override fun sendMailToNewbie(recipient: String, nickname: String, createdAt: String) {

        message.addRecipients(Message.RecipientType.TO, recipient)
        message.setSubject("[zziririt] 회원 가입이 완료되었습니다.")
        val msg = """
            <h1>[회원가입 정보]</h1> <br>
            <h2> 닉네임: $nickname <br>
            <h2> 가입일: $createdAt
        """.trimIndent()
        message.setText(msg, "UTF-8", "HTML")
        message.setFrom(zziririt)
        javaMailSender.send(message)

    }
}