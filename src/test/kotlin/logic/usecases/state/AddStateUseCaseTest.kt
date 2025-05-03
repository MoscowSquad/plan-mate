package logic.usecases.state

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.StateRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.IllegalStateTitle
import java.util.*

class AddStateUseCaseTest {
 private lateinit var stateRepository: StateRepository

 private lateinit var addStateUseCase: AddStateUseCase

 @BeforeEach
 fun setUp() {
  stateRepository = mockk()
  addStateUseCase = AddStateUseCase(stateRepository)
 }

 @Test
 fun `should return true when state is successfully added`() {
  // Given
  val validState = TaskState(
   id = UUID.randomUUID(),
   title = "Valid State",
   projectId = UUID.randomUUID()
  )

  every { stateRepository.add(validState.projectId, validState) } returns true

  // When
  val result = addStateUseCase(validState)

  // Then
  assertTrue(result)
  verify(exactly = 1) { stateRepository.add(validState.projectId, validState) }
 }

 @Test
 fun `should throw IllegalStateTitle when title is blank`() {
  // Given
  val invalidState = TaskState(
   id = UUID.randomUUID(),
   title = "",
   projectId = UUID.randomUUID()
  )

  // When & Then
  val exception = assertThrows<IllegalStateTitle> {
   addStateUseCase(invalidState)
  }

  assertEquals("Task state title cannot be blank", exception.message)
  verify(exactly = 0) { stateRepository.add(any(), any()) }
 }

 @Test
 fun `should throw IllegalStateTitle when title is too long`() {
  // Given
  val longTitle = "a".repeat(101)
  val invalidState = TaskState(
   id = UUID.randomUUID(),
   title = longTitle,
   projectId = UUID.randomUUID()
  )

  // When & Then
  val exception = assertThrows<IllegalStateTitle> {
   addStateUseCase(invalidState)
  }

  assertTrue(exception.message!!.contains("title"))
  verify(exactly = 0) { stateRepository.add(any(), any()) }
 }

 @Test
 fun `should throw when repository fails to add`() {
  // Given
  val validState = TaskState(
   id = UUID.randomUUID(),
   title = "Valid",
   projectId = UUID.randomUUID()
  )

  every { stateRepository.add(validState.projectId, validState) } returns false

  // When & Then
  assertThrows<IllegalStateException> {
   addStateUseCase(validState)
  }

  verify(exactly = 1) { stateRepository.add(validState.projectId, validState) }
 }
}