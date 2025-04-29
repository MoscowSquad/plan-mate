package logic.usecases

import logic.models.State
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Method
import java.util.*
import kotlin.test.Test

class DeleteStateUseCaseTest {

    private lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeEach
    fun setUp() {
        deleteStateUseCase = DeleteStateUseCase()
    }

    @Test
    fun `invoke should return true when correctly deleted`() {
        // Given
        val stateId = UUID.randomUUID()
        val useCase = DeleteStateUseCase()

        // When
        val result = useCase(stateId)

        // Then
        assertTrue(result)
    }

    @Test
    fun `invoke should throw IllegalStateException when state does not exist`() {
        // Given
        val nonExistentStateId = UUID.randomUUID()
        val useCase = DeleteStateUseCase()

        // When
        val exception = assertThrows<IllegalStateException> {
            useCase(nonExistentStateId)
        }
        // Then
        assertEquals(
            "State with ID $nonExistentStateId does not exist",
            exception.message
        )
    }

    @Test
    fun `isStateExist should return false when state not exist in specific project`() {
        val stateId = UUID.randomUUID()
        val method = getPrivateMethod("isStateExist", UUID::class.java)
        val result = method.invoke(deleteStateUseCase, stateId) as Boolean
        assertFalse(result)
    }

    @Test
    fun `getState should return State with matching ID`() {
        val stateId = UUID.randomUUID()
        val method = getPrivateMethod("getState", UUID::class.java)
        val result = method.invoke(deleteStateUseCase, stateId) as State

        assertEquals(stateId, result.id)
    }

    @Test
    fun `getState should return State with title 'nre'`() {
        // Given
        val stateId = UUID.randomUUID()
        val method = getPrivateMethod("getState", UUID::class.java)

        // When
        val result = method.invoke(deleteStateUseCase, stateId) as State

        // Then
        assertEquals("nre", result.title)
    }

    @Test
    fun `getState should return State with non-null projectId`() {
        // Given
        val stateId = UUID.randomUUID()
        val method = getPrivateMethod("getState", UUID::class.java)
        // When
        val result = method.invoke(deleteStateUseCase, stateId) as State
        // Then
        assertNotNull(result.projectId)
    }

    @Test
    fun `getState should return State with random project ID`() {
        // Give
        val stateId = UUID.randomUUID()
        val method = getPrivateMethod("getState", UUID::class.java)

        // When
        val result1 = method.invoke(deleteStateUseCase, stateId) as State
        val result2 = method.invoke(deleteStateUseCase, stateId) as State
        // Then
        assertNotEquals(result1.projectId, result2.projectId)
    }

    private fun getPrivateMethod(methodName: String, vararg parameterTypes: Class<*>): Method {
        val method = DeleteStateUseCase::class.java.getDeclaredMethod(methodName, *parameterTypes)
        method.isAccessible = true
        return method
    }
}
