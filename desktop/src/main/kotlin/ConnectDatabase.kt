import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun connect() {
        val database = Database.connect(
            url = DatabaseConfig.URL,
            driver = DatabaseConfig.DRIVER
        )

        transaction(database) {
            SchemaUtils.create(Websites, CaptureImages)
        }
    }
}

object DatabaseConfig {
    const val DRIVER = "org.sqlite.JDBC"
    const val URL = "jdbc:sqlite:./data.db"
}
