package com.mycompany.inventory

import io.javalin.Javalin.log
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.InternalServerErrorResponse
import io.javalin.http.NotFoundResponse
import io.javalin.plugin.json.JavalinJson
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isEqualTo
import org.valiktor.functions.isPositiveOrZero
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import java.util.*

object InventoryController {

    val locationToProduct = mutableMapOf(
            5L to mutableSetOf(1L, 2L, 3L)
    )

    val inventory = mutableMapOf(
            Pair(5L,1L) to Inventory(5,1, 5),
            Pair(5L,2L) to Inventory(5,2, 5),
            Pair(5L,3L) to Inventory(5,3, 5),
    )

    fun create(ctx: Context) {
        val newInventory: Inventory = ctx.body<Inventory>()
        try {
            validate(newInventory) {
                validate(Inventory::quantity).isPositiveOrZero()
            }
        } catch (ex: ConstraintViolationException) {
            val msg = ex.constraintViolations.mapToMessage(baseName = "messages", locale = Locale.ENGLISH)
                    .map { it.property to it.message}
                    .toMap()
            throw BadRequestResponse(JavalinJson.toJson(msg))
        }

        locationToProduct.getOrPut(newInventory.locationId, { mutableSetOf() }).add(newInventory.productId)

        inventory[Pair(newInventory.locationId, newInventory.productId)] = newInventory
        ctx.status(201)
        ctx.json(newInventory)
    }

    fun put(ctx: Context) {
        val locationId = ctx.pathParam("locationId").toLong()
        val updatedInventory: Inventory = ctx.body<Inventory>()

        try {
            validate(updatedInventory) {
                validate(Inventory::locationId).isEqualTo(locationId)
                validate(Inventory::quantity).isPositiveOrZero()
            }
        } catch (ex: ConstraintViolationException) {
            val msg = ex.constraintViolations.mapToMessage(baseName = "messages", locale = Locale.ENGLISH)
                    .map { it.property to it.message}
                    .toMap()
            throw BadRequestResponse(JavalinJson.toJson(msg))
        }

        inventory.getOrElse(Pair(updatedInventory.locationId, updatedInventory.productId), {
            throw NotFoundResponse("There is no inventory for locationId:$locationId")
        })

        inventory[Pair(updatedInventory.locationId, updatedInventory.productId)] = updatedInventory
        ctx.json(updatedInventory)
    }

    fun getAll(ctx: Context) {
        ctx.json(inventory.values.toList())
    }

    fun get(ctx: Context) {
        val locationId = ctx.pathParam("locationId").toLong()

        val products: MutableSet<Long> = locationToProduct.getOrElse(locationId, {
            throw NotFoundResponse("There is no inventory for locationId:$locationId")
        })

        val result = mutableListOf<Inventory>()
        for(product in products){
            result.add(
                    inventory.getOrElse(
                            Pair(locationId, product),
                            { throw InternalServerErrorResponse() })
            )
        }

        ctx.json(result)
    }
}