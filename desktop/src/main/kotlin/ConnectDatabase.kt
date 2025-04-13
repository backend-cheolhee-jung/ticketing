import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun connect() {
        transaction {
            Database.connect(
                url = DatabaseConfig.URL,
                driver = DatabaseConfig.DRIVER
            )
        }
    }
}

object DatabaseConfig {
    const val DRIVER = "org.sqlite.JDBC"
    const val URL = "jdbc:sqlite:./data.db"
}
