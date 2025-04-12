data class Mail(
    val to: String,
    val subject: String,
    val body: String,
) {
    companion object {
        @JvmStatic
        fun of(
            to: String,
            subject: String,
            body: String,
        ) = Mail(
            to = to,
            subject = subject,
            body = body
        )
    }
}