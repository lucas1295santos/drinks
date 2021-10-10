package data

import org.jetbrains.exposed.sql.Database
import java.net.URI

object DB {
    private val host: String
    private val dbName: String
    private val dbUser: String
    private val dbPassword: String
    private val port: Int

    init {
        val dbUrl = System.getenv("DATABASE_URL")
        if(dbUrl != null) {
            val uri = URI(dbUrl)
            host = uri.host
            port = uri.port
            dbName = uri.path.substring(1)
            val userInfo = uri.userInfo.split(":")
            dbUser = userInfo[0]
            dbPassword = userInfo[1]
        } else {
            host = System.getenv("DB_HOST")
            dbName = System.getenv("DB_NAME")
            dbUser = System.getenv("DB_USER")
            dbPassword = System.getenv("DB_PASSWORD")
            port = System.getenv("DB_PORT").toInt()
        }
    }

    fun connect(): Database {
        return Database.connect(
            "jdbc:postgresql://$host:$port/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
    }

}