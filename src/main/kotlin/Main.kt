import com.fasterxml.jackson.databind.SerializationFeature
import data.DB
import data.Drinks
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import route.registerDrinkRoutes

fun main() {
    val port = System.getenv("PORT").toIntOrNull() ?: 8080

    val server = embeddedServer(Netty, port, module = Application::mainModule)

    server.start()
}

fun Application.mainModule() {
    DB.connect()

    transaction {
        SchemaUtils.create(Drinks)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    registerDrinkRoutes()
}