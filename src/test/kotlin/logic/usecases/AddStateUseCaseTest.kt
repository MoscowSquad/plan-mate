package logic.usecases

import logic.models.Project
import logic.models.State
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AddStateUseCaseTest{
 private lateinit var addStateUseCase: AddStateUseCase
 @BeforeEach
 fun setup(){
   addStateUseCase = AddStateUseCase()
 }

  @Test
  fun `should return true if state add correctly`(){
   // Given
   val newState = State(
    id = UUID.randomUUID(),
    title = "TODO",
    projectId = UUID.randomUUID()
   )

   // When
   val result = addStateUseCase.invoke(newState)

   // Then
   assertTrue(result)

  }

 @Test
 fun `should be throw exception if project id is not exist`(){
  // Given
  val newState = State(
   id = UUID.randomUUID(),
   title = "TODO",
   projectId = UUID.randomUUID() // suppose not exist id
  )

  // When && Then
  org.junit.jupiter.api.assertThrows<Exception> {
    addStateUseCase.invoke(newState)
    Exception("the project id should be valid")
  }
 }

 @Test
 fun `should return false when invalid title`() {
  // Given
  val invalidState = State(
   id = UUID.randomUUID(),
   title = "", // Invalid empty title
   projectId = UUID.randomUUID()
  )

  // When
  val result = addStateUseCase.invoke(invalidState)
  // Then
  assertFalse(result)

 }

 @Test
 fun `isProjectExist should return false for project not exist`() {

  val method = AddStateUseCase::class.java.getDeclaredMethod("isProjectExist", Project::class.java)
  method.isAccessible = true

  // Given
  val testProject = Project(
   name = "Plan-mate",
   id = UUID.randomUUID()
  )

  // When
  val result = method.invoke(addStateUseCase, testProject) as Boolean

  // Then
  assertFalse(result)
 }

 @Test
 fun `isValidTitleState should return false for title not valid`() {

  val method = AddStateUseCase::class.java.getDeclaredMethod("isValidTitleState", String::class.java)
  method.isAccessible = true

  // Given
  val testTitle = "Test Title"

  // When
  val result = method.invoke(addStateUseCase, testTitle) as Boolean

  // Then
  assertFalse(result)
 }
}

