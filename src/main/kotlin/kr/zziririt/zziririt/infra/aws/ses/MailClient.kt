package kr.zziririt.zziririt.infra.aws.ses

import aws.sdk.kotlin.services.ses.model.SendEmailResponse

interface MailClient {
    suspend fun send(sender: String?, recipient: String, subjectVal: String?, bodyHTML: String?): SendEmailResponse
}