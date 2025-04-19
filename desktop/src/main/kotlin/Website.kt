import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.trim

object Websites : Table("website_urls") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255).uniqueIndex().check { it.trim() neq "" }
    val url = varchar("url", 255).uniqueIndex("url_unique").check { it.trim() neq "" }
    val loginUrl = varchar("login_url", 255).check { it.trim() neq "" }
    val email = varchar("email", 255).check { it.trim() neq "" }.check { it like "%@%" }
    val password = varchar("password", 255).check { it.trim() neq "" }
    val idInput = varchar("id_input", 255).check { it.trim() neq "" }
    val passwordInput = varchar("password_input", 255).check { it.trim() neq "" }
    val loginButtonElement = varchar("login_button_element", 255).check { it.trim() neq "" }

    override val primaryKey = PrimaryKey(id)
}

data class Website(
    val name: String,
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
            name: String,
            url: String,
            loginUrl: String,
            email: String,
            password: String,
            idInput: String,
            passwordInput: String,
            loginButtonElement: String,
        ) = Website(
            name = name,
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
                name = resultRow[Websites.name],
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