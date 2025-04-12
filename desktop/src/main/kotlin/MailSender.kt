interface MailSender {
    suspend fun send(mail: Mail)
}