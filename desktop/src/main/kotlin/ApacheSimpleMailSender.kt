import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class ApacheSimpleMailSender(
    private val simpleEmail: SimpleEmail,
) : MailSender {
    override suspend fun send(mail: Mail): Unit =
        withContext(Dispatchers.IO) {
            simpleEmail.apply {
                hostName = "smtp.googlemail.com"
                setSmtpPort(465)
                authenticator = DefaultAuthenticator("email-account", "account-password")
                isSSLOnConnect = true
                setFrom("ekxk1234@gmail.com")
                subject = mail.subject
                setMsg(mail.body)
                send()
            }
        }
}