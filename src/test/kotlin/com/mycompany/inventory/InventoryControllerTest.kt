package com.mycompany.inventory

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify


class InventoryControllerTest {

    private val ctx = Mockito.mock(Context::class.java)
    private val INVENTORY_01 = Inventory(1,1, 5)

    @BeforeEach
    fun beforeEach() {
        InventoryController.locationToProduct.clear()
        InventoryController.inventory.clear()
    }

    @Test
    fun `GET for single inventory gives 200`() {
        InventoryController.locationToProduct[1L] = mutableSetOf(1L)
        InventoryController.inventory[Pair(1L,1L)] = INVENTORY_01
        Mockito.`when`(ctx.pathParam("locationId")).thenReturn("1")

        InventoryController.get(ctx)
        verify(ctx).json(listOf(INVENTORY_01))
    }

    @Test
    fun `GET for single inventory throws for location`() {
        InventoryController.locationToProduct[1L] = mutableSetOf(1L)
        InventoryController.inventory[Pair(1L,1L)] = INVENTORY_01
        Mockito.`when`(ctx.pathParam("locationId")).thenReturn("2")

        assertThrows(NotFoundResponse::class.java) { InventoryController.get(ctx) }
    }

    @Test
    fun `POST to CREATE inventory gives 200`() {
        Mockito.`when`(ctx.body<Inventory>()).thenReturn(INVENTORY_01)

        InventoryController.create(ctx)
        verify(ctx).status(201)
        assertThat(InventoryController.inventory[Pair(1L,1L)]).isEqualTo(INVENTORY_01)
    }

    @Test
    fun `POST to CREATE inventory throws for negative quantity`() {
        Mockito.`when`(ctx.body<Inventory>()).thenReturn(Inventory(1,1, -1))

        assertThrows(BadRequestResponse::class.java) { InventoryController.create(ctx) }
        assertThat(InventoryController.inventory.containsKey(Pair(1L,1L))).isFalse
    }

    @Test
    fun `PUT to UPDATE inventory gives 200`() {
        InventoryController.locationToProduct[1L] = mutableSetOf(1L)
        InventoryController.inventory[Pair(1L,1L)] = INVENTORY_01
        val updatedInventory = Inventory(1,1, 10)
        Mockito.`when`(ctx.pathParam("locationId")).thenReturn("1")
        Mockito.`when`(ctx.body<Inventory>()).thenReturn(updatedInventory)

        InventoryController.put(ctx)
        verify(ctx).json(updatedInventory)
        assertThat(InventoryController.inventory[Pair(1L,1L)]).isEqualTo(updatedInventory)
    }

    @Test
    fun `PUT to UPDATE inventory throws for negative quantity`() {
        InventoryController.locationToProduct[1L] = mutableSetOf(1L)
        InventoryController.inventory[Pair(1L,1L)] = INVENTORY_01
        Mockito.`when`(ctx.pathParam("locationId")).thenReturn("1")
        Mockito.`when`(ctx.body<Inventory>()).thenReturn(Inventory(1,1, -1))

        assertThrows(BadRequestResponse::class.java) { InventoryController.put(ctx) }
        assertThat(InventoryController.inventory[Pair(1L,1L)]).isEqualTo(INVENTORY_01)
    }

    @Test
    fun `PUT to UPDATE inventory throws for unknown product`() {
        InventoryController.locationToProduct[1L] = mutableSetOf(1L)
        InventoryController.inventory[Pair(1L,1L)] = INVENTORY_01
        Mockito.`when`(ctx.pathParam("locationId")).thenReturn("1")
        Mockito.`when`(ctx.body<Inventory>()).thenReturn(Inventory(1,2, 1))

        assertThrows(NotFoundResponse::class.java) { InventoryController.put(ctx) }
    }
}