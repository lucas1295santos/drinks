package data

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Drinks: IntIdTable() {
    val name = varchar("name", length = 255)
    val description = varchar("description", length = 1000)
}

class DrinkEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<DrinkEntity>(Drinks)

    var name by Drinks.name
    var description by Drinks.description

    override fun toString(): String {
        return "Drink($name, $description)"
    }

    fun toDrink() = Drink(id.value, name, description)
}

data class Drink(
    val id: Int,
    val name: String,
    val description: String?
)