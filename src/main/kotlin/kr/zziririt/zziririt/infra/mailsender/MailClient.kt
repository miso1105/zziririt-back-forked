package kr.zziririt.zziririt.infra.mailsender

interface MailClient {
    fun sendMailToNewbie(recipient: String, nickname: String, createdAt: String){
    }
}