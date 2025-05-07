package presentation.audit

import io.mockk.*
import logic.models.AuditType
import logic.usecases.audit.AddAuditLogUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class AddAuditLogUITest {
    private lateinit var addAuditLogUseCase: AddAuditLogUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var addAuditLogUI: AddAuditLogUI

    private val validUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val validEntityId = UUID.fromString("223e4567-e89b-12d3-a456-426614174001")
    private val validUserIdString = validUserId.toString()
    private val validEntityIdString = validEntityId.toString()
    private val validAction = "CREATE"

    @BeforeEach
    fun setUp() {
        addAuditLogUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        addAuditLogUI = AddAuditLogUI(addAuditLogUseCase, consoleIO)
    }

    @Test
    fun `should return to main menu when option 2 is selected`() {
        // Given
        every { consoleIO.read() } returns "2"

        // When
        addAuditLogUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should handle invalid menu selection`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("invalid", "2")

        // When
        addAuditLogUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\nInvalid input. Please enter 1 or 2.")
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should create audit log successfully with valid inputs for TASK type`() {
        // Given
        val inputSequence = listOf(
            "1",
            validUserIdString,
            validEntityIdString,
            validAction,
            "1",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence

        // When
        addAuditLogUI.invoke()

        // Then
        verify(exactly = 2) {
            addAuditLogUseCase.invoke(withArg { auditLog ->
                auditLog.userId == validUserId &&
                        auditLog.entityId == validEntityId &&
                        auditLog.action == validAction &&
                        auditLog.auditType == AuditType.TASK
            })
        }
        verify { consoleIO.write("\n✅ Audit log created successfully!") }
    }

    @Test
    fun `should create audit log successfully with valid inputs for PROJECT type`() {
        // Given
        val inputSequence = listOf(
            "1",
            validUserIdString,
            validEntityIdString,
            validAction,
            "2",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence

        // When
        addAuditLogUI.invoke()

        // Then
        verify(exactly = 2) {
            addAuditLogUseCase.invoke(withArg { auditLog ->
                auditLog.userId == validUserId &&
                        auditLog.entityId == validEntityId &&
                        auditLog.action == validAction &&
                        auditLog.auditType == AuditType.PROJECT
            })
        }
    }

    @Test
    fun `should handle invalid UUID format for user ID`() {
        // Given
        val inputSequence = listOf(
            "1",
            "invalid-uuid",
            validUserIdString,
            validEntityIdString,
            validAction,
            "1",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence

        // When
        addAuditLogUI.invoke()

        // Then
        verify { consoleIO.write("Invalid UUID format. Please try again.") }
        verify(exactly = 2) { addAuditLogUseCase.invoke(any()) }
    }

    @Test
    fun `should handle invalid UUID format for entity ID`() {
        // Given
        val inputSequence = listOf(
            "1",
            validUserIdString,
            "invalid-uuid",
            validEntityIdString,
            validAction,
            "1",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence

        // When
        addAuditLogUI.invoke()

        // Then
        verify { consoleIO.write("Invalid UUID format. Please try again.") }
        verify(exactly = 2) { addAuditLogUseCase.invoke(any()) }
    }

    @Test
    fun `should handle invalid audit type selection`() {
        // Given
        val inputSequence = listOf(
            "1",
            validUserIdString,
            validEntityIdString,
            validAction,
            "invalid",
            "1",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence

        // When
        addAuditLogUI.invoke()

        // Then
        verify { consoleIO.write("Invalid selection. Please try again.") }
        verify(exactly = 2) { addAuditLogUseCase.invoke(any()) }
    }

    @Test
    fun `should handle empty action input`() {
        // Given
        val inputSequence = listOf(
            "1",
            validUserIdString,
            validEntityIdString,
            "",
            validAction,
            "1",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence

        // When
        addAuditLogUI.invoke()

        // Then
        verify { consoleIO.write("This field is required. Please try again.") }
        verify(exactly = 2) { addAuditLogUseCase.invoke(any()) }
    }

    @Test
    fun `should handle exception when creating audit log`() {
        // Given
        val errorMessage = "Database connection failed"
        val inputSequence = listOf(
            "1",
            validUserIdString,
            validEntityIdString,
            validAction,
            "1",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence
        every { addAuditLogUseCase.invoke(any()) } throws RuntimeException(errorMessage)

        // When
        addAuditLogUI.invoke()

        // Then
        verify { consoleIO.write("\n❌ Error creating audit log: $errorMessage") }
    }

    @Test
    fun `should accept optional empty input`() {
        // Given
        val readInputMethod = AddAuditLogUI::class.java.getDeclaredMethod(
            "readInput",
            String::class.java,
            Boolean::class.java
        ).apply { isAccessible = true }

        every { consoleIO.write(any()) } just Runs
        every { consoleIO.read() } returns ""

        // When
        val result = readInputMethod.invoke(addAuditLogUI, "Enter optional field:", true) as String

        // Then
        verify { consoleIO.write("Enter optional field:") }
        verify { consoleIO.read() }
        assert(result.isEmpty())
    }

    @Test
    fun `should require non-empty input when optional is false`() {
        // Given
        val readInputMethod = AddAuditLogUI::class.java.getDeclaredMethod(
            "readInput",
            String::class.java,
            Boolean::class.java
        ).apply { isAccessible = true }

        every { consoleIO.write(any()) } just Runs
        every { consoleIO.read() } returnsMany listOf("", "some value")

        // When
        val result = readInputMethod.invoke(addAuditLogUI, "Enter required field:", false) as String

        // Then
        verifySequence {
            consoleIO.write("Enter required field:")
            consoleIO.read()
            consoleIO.write("This field is required. Please try again.")
            consoleIO.write("Enter required field:")
            consoleIO.read()
        }
        assert(result == "some value")
    }

    @Test
    fun `should test the full sequence with multiple interactions`() {
        // Given
        val inputSequence = listOf(
            "invalid",
            "1",
            "invalid",
            validUserIdString,
            "invalid",
            validEntityIdString,
            "",
            validAction,
            "3",
            "1",
            "2"
        )
        every { consoleIO.read() } returnsMany inputSequence

        // When
        addAuditLogUI.invoke()

        // Then
        verify { consoleIO.write("\nInvalid input. Please enter 1 or 2.") }
        verify { consoleIO.write("Invalid UUID format. Please try again.") }
        verify { consoleIO.write("Invalid UUID format. Please try again.") }
        verify { consoleIO.write("This field is required. Please try again.") }
        verify { consoleIO.write("Invalid selection. Please try again.") }
        verify(exactly = 2) { addAuditLogUseCase.invoke(any()) }
    }
}