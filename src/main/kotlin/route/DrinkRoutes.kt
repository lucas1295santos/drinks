package route

import data.Drink
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.di
import service.DrinkService
import service.DrinkServiceDB

fun Route.getDrinkService(): DrinkService {
    val drinkService by di().instance<DrinkServiceDB>()
    return drinkService
}

fun Route.listOfDrinksRoute() {
    get("/drinks") {
        val drinks = this@listOfDrinksRoute.getDrinkService().getAll()
        call.respond(drinks)
    }
}

fun Route.createDrinkRoute() {
    post("/drinks") {
        val drink = call.receive<Drink>()
        this@createDrinkRoute.getDrinkService().create(drink)
        call.response.status(HttpStatusCode.Created)
    }
}

fun Route.getByIdRoute() {
    get("/drinks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
            "Missing or malformed ID",
            status = HttpStatusCode.BadRequest
        )
        val drink = this@getByIdRoute.getDrinkService().getById(id) ?: return@get call.respondText(
            "No drink with id $id",
            status = HttpStatusCode.NotFound
        )
        call.respond(drink)
    }
}

fun Route.deleteByIdRoute() {
    delete("/drinks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
            "Missing or malformed ID",
            status = HttpStatusCode.BadRequest
        )
        if (this@deleteByIdRoute.getDrinkService().delete(id) == null) { // TODO("Do proper error handling")
            call.respondText(
                "No drink with id $id",
                status = HttpStatusCode.NotFound
            )
        } else {
            call.respondText(
                "Drink removed",
                status = HttpStatusCode.Accepted
            )
        }
    }
}

// TODO: 17/07/21 Extract id error handling
fun Route.updateByIdRoute() {
    put("/drinks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText(
            "Missing or malformed ID",
            status = HttpStatusCode.BadRequest
        )
        val drink = call.receive<Drink>()
        if (this@updateByIdRoute.getDrinkService().update(drink) == null) {
            call.respondText(
                "No drink with id $id",
                status = HttpStatusCode.NotFound
            )
        } else {
            call.respondText(
                "Drink updated",
                status = HttpStatusCode.Accepted
            )
        }
    }
}

fun Application.registerDrinkRoutes() {
    routing {
        listOfDrinksRoute()
        createDrinkRoute()
        getByIdRoute()
        deleteByIdRoute()
        updateByIdRoute()
    }
}