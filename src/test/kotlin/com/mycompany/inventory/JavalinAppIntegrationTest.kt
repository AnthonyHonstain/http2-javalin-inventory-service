package com.mycompany.inventory

import io.javalin.plugin.json.JavalinJson
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test


class JavalinAppIntegrationTest {

    private val app = JavalinApp()

    private val INVENTORY_01 = Inventory(1,1, 5)
    private val INVENTORY_02 = Inventory(1,2, 15)

    @BeforeEach
    fun beforeEach() {
        InventoryController.inventory.clear()
    }

    @Test
    fun `GET ALL to fetch all inventorys`() {
        InventoryController.inventory[Pair(INVENTORY_01.locationId, INVENTORY_01.productId)] = INVENTORY_01
        InventoryController.inventory[Pair(INVENTORY_02.locationId, INVENTORY_02.productId)] = INVENTORY_02
        app.start(1234)
        val response: HttpResponse<String> = Unirest.get("http://localhost:1234/inventory").asString()

        assertThat(response.status).isEqualTo(200)
        val inventoryListJson = JavalinJson.toJson(InventoryController.inventory.values.toList())
        assertThat(response.body).isEqualTo(inventoryListJson)
        app.stop()
    }

    @Test
    fun `GET specific inventory`() {
        InventoryController.inventory[Pair(INVENTORY_01.locationId, INVENTORY_01.productId)] = INVENTORY_01
        app.start(1235)
        val response: HttpResponse<String> = Unirest.get("http://localhost:1235/inventory/${INVENTORY_01.productId}").asString()

        assertThat(response.status).isEqualTo(200)
        assertThat(response.body).isEqualTo(JavalinJson.toJson(listOf(INVENTORY_01)))
        app.stop()
    }

    @Test
    fun `POST to create inventory`() {
        app.start(1236)
        val body: String = JavalinJson.toJson(INVENTORY_01)
        val response: HttpResponse<String> = Unirest.post("http://localhost:1236/inventory/").body(body).asString()

        assertThat(response.status).isEqualTo(201)
        assertThat(response.body).isEqualTo(JavalinJson.toJson(INVENTORY_01))
        app.stop()
    }

    @Test
    fun `PUT to update inventory`() {
        InventoryController.inventory[Pair(1L,1L)] = INVENTORY_01
        app.start(1237)
        val updatedInventory = Inventory(1,1, 10)
        val body: String = JavalinJson.toJson(updatedInventory)
        val response: HttpResponse<String> = Unirest.put("http://localhost:1237/inventory/${INVENTORY_01.productId}").body(body).asString()

        assertThat(response.status).isEqualTo(200)
        assertThat(response.body).isEqualTo(JavalinJson.toJson(updatedInventory))
        app.stop()
    }
}