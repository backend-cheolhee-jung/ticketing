import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun connect() {
        Database.connect(
            url = DatabaseConfig.URL,
            driver = DatabaseConfig.DRIVER
        )
    }
}

object DatabaseConfig {
    const val DRIVER = "org.sqlite.JDBC"
    const val URL = "jdbc:sqlite:./data.db"
}
