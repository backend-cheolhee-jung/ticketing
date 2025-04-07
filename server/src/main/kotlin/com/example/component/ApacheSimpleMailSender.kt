package com.example.component

import com.example.util.model.Mail
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class ApacheSimpleMailSender(
    private val simpleEmail: SimpleEmail,
) : MailSender {
    override suspend fun send(mail: Mail) {
        simpleEmail.apply {
            hostName = "smtp.googlemail.com"
            setSmtpPort(465)
            // TODO: 계정 정보는 환경변수로 관리하자.
            authenticator = DefaultAuthenticator("email-account", "account-password")
            isSSLOnConnect = true
            setFrom("ekxk1234@gmail.com")
            subject = mail.subject
            setMsg(mail.body)
            send()
        }
    }
}