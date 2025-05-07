package logic.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class AuditEntityTest {

    @Test
    fun `constructor creates instance with correct values`() {
        val id = UUID.randomUUID()
        val name = "Test Entity"

        val entity = AuditEntity(id, name)

        assertEquals(id, entity.id)
        assertEquals(name, entity.name)
    }

    @Test
    fun `equals returns true for identical entities`() {
        val id = UUID.randomUUID()
        val name = "Test Entity"

        val entity1 = AuditEntity(id, name)
        val entity2 = AuditEntity(id, name)

        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
    }

    @Test
    fun `equals returns false for entities with different ids`() {
        val name = "Test Entity"

        val entity1 = AuditEntity(UUID.randomUUID(), name)
        val entity2 = AuditEntity(UUID.randomUUID(), name)

        assertNotEquals(entity1, entity2)
    }

    @Test
    fun `equals returns false for entities with different names`() {
        val id = UUID.randomUUID()

        val entity1 = AuditEntity(id, "Entity 1")
        val entity2 = AuditEntity(id, "Entity 2")

        assertNotEquals(entity1, entity2)
    }

    @Test
    fun `copy creates new instance with updated values`() {
        val originalId = UUID.randomUUID()
        val originalName = "Original Entity"
        val entity = AuditEntity(originalId, originalName)

        val newId = UUID.randomUUID()
        val newName = "New Entity"

        val copiedWithNewId = entity.copy(id = newId)
        assertEquals(newId, copiedWithNewId.id)
        assertEquals(originalName, copiedWithNewId.name)

        val copiedWithNewName = entity.copy(name = newName)
        assertEquals(originalId, copiedWithNewName.id)
        assertEquals(newName, copiedWithNewName.name)

        val copiedWithBoth = entity.copy(id = newId, name = newName)
        assertEquals(newId, copiedWithBoth.id)
        assertEquals(newName, copiedWithBoth.name)
    }

    @Test
    fun `toString returns formatted string representation`() {
        val id = UUID.randomUUID()
        val name = "Test Entity"
        val entity = AuditEntity(id, name)

        val expectedString = "AuditEntity(id=$id, name=$name)"
        assertEquals(expectedString, entity.toString())
    }
}