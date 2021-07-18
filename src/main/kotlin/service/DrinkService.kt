package service

import data.Drink
import data.DrinkEntity
import org.jetbrains.exposed.sql.transactions.transaction

interface DrinkService {
    suspend fun create(drink: Drink): Int
    suspend fun getAll(): List<Drink>
    suspend fun getById(id: Int): Drink?
    suspend fun delete(id: Int): Int?
    suspend fun update(drink: Drink): Drink?
}

class DrinkServiceDB: DrinkService {
    override suspend fun create(drink: Drink): Int {
        return transaction {
            DrinkEntity.new {
                this.name = drink.name
                this.description = drink.description ?: ""
            }.id.value
        }
    }

    override suspend fun getAll(): List<Drink> {
        return transaction {
            DrinkEntity.all().map(DrinkEntity::toDrink)
        }
    }

    override suspend fun getById(id: Int): Drink? {
        return transaction {
            DrinkEntity.findById(id)?.toDrink()
        }
    }

    override suspend fun delete(id: Int): Int? {
        return transaction {
            val entity = DrinkEntity.findById(id) ?: return@transaction null
            entity.delete()
            return@transaction id
        }
    }

    override suspend fun update(drink: Drink): Drink? {
        return transaction {
            val entity = DrinkEntity.findById(drink.id) ?: return@transaction null
            entity.name = drink.name
            if (drink.description != null) entity.description = drink.description
            return@transaction entity.toDrink()
        }
    }
}