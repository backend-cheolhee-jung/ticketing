import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Websites: Table("website_urls") {
    val id = long("id").autoIncrement()
    val url = varchar("url", 255).uniqueIndex("url_unique")
    val loginUrl = varchar("login_url", 255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val idInput = varchar("id_input", 255)
    val passwordInput = varchar("password_input", 255)
    val loginButtonElement = varchar("login_button_element", 255)

    override val primaryKey = PrimaryKey(id)
}

data class Website(
    val url: String,
    val loginUrl: String,
    val email: String,
    val password: String,
    val idInput: String,
    val passwordInput: String,
    val loginButtonElement: String,
) {
    companion object {
        @JvmStatic
        fun of(
            url: String,
            loginUrl: String,
            email: String,
            password: String,
            idInput: String,
            passwordInput: String,
            loginButtonElement: String,
        ) = Website(
            url = url,
            loginUrl = loginUrl,
            email = email,
            password = password,
            idInput = idInput,
            passwordInput = passwordInput,
            loginButtonElement = loginButtonElement,
        )

        @JvmStatic
        fun of(
            resultRow: ResultRow,
        ) =
            Website(
                url = resultRow[Websites.url],
                loginUrl = resultRow[Websites.loginUrl],
                email = resultRow[Websites.email],
                password = resultRow[Websites.password],
                idInput = resultRow[Websites.idInput],
                passwordInput = resultRow[Websites.passwordInput],
                loginButtonElement = resultRow[Websites.loginButtonElement],
            )
    }
}