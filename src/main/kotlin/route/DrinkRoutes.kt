package route

import data.Drink
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import service.DrinkServiceDB

fun Route.listOfDrinksRoute() {
    get("/drinks") {
        val drinks = DrinkServiceDB().getAll()
        call.respond(drinks)
    }
}

fun Route.createDrinkRoute() {
    post("/drinks") {
        val drink = call.receive<Drink>()
        DrinkServiceDB().create(drink) // TODO: Inject service dependency
        call.response.status(HttpStatusCode.Created)
    }
}

fun Route.getById() {
    get("/drinks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
            "Missing or malformed ID",
            status = HttpStatusCode.BadRequest
        )
        val drink = DrinkServiceDB().getById(id) ?: return@get call.respondText(
            "No drink with id $id",
            status = HttpStatusCode.NotFound
        )
        call.respond(drink)
    }
}

fun Route.deleteById() {
    delete("/drinks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
            "Missing or malformed ID",
            status = HttpStatusCode.BadRequest
        )
        if (DrinkServiceDB().delete(id) == null) { // TODO("Do proper error handling")
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
fun Route.updateById() {
    put("/drinks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText(
            "Missing or malformed ID",
            status = HttpStatusCode.BadRequest
        )
        val drink = call.receive<Drink>()
        if (DrinkServiceDB().update(drink) == null) {
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
        getById()
        deleteById()
        updateById()
    }
}