package domain.usecases.task

    import domain.models.Task
    import domain.repositories.TasksRepository
    import io.mockk.coEvery
    import io.mockk.coVerify
    import io.mockk.mockk
    import kotlinx.coroutines.test.runTest
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import java.util.*
    import kotlin.test.assertFalse
    import kotlin.test.assertTrue

class AddTaskUseCaseTest {
        private lateinit var addTaskUseCase: AddTaskUseCase
        private lateinit var tasksRepository: TasksRepository

        @BeforeEach
        fun setup() {
            tasksRepository = mockk()
            addTaskUseCase = AddTaskUseCase(tasksRepository)
        }

        @Test
        fun `should return true when add task without any issue`() = runTest {
            // Given
            val task = Task(
                id = UUID.randomUUID(),
                title = "Task Title",
                projectId = UUID.randomUUID(),
                description = "Task description",
                stateId = UUID.randomUUID(),
                subTasks = listOf()
            )
            coEvery { tasksRepository.addTask(task) } returns true

            // When
            val result = addTaskUseCase(task)

            // Then
            assertTrue(result)
            coVerify(exactly = 1) { tasksRepository.addTask(task) }
        }

        @Test
        fun `should return false when repository fails to add task`() = runTest {
            // Given
            val task = Task(
                id = UUID.randomUUID(),
                title = "Task Title",
                projectId = UUID.randomUUID(),
                description = "Task description",
                stateId = UUID.randomUUID(),
                subTasks = listOf()
            )
            coEvery { tasksRepository.addTask(task) } returns false

            // When
            val result = addTaskUseCase(task)

            // Then
            assertFalse(result)
            coVerify(exactly = 1) { tasksRepository.addTask(task) }
        }

        @Test
        fun `should pass correct Task object to repository`() = runTest {
            // Given
            val taskId = UUID.randomUUID()
            val projectId = UUID.randomUUID()
            val stateId = UUID.randomUUID()
            val task = Task(
                id = taskId,
                title = "Test Task",
                projectId = projectId,
                description = "Test description",
                stateId = stateId,
                subTasks = listOf()
            )
            coEvery { tasksRepository.addTask(any()) } returns true

            // When
            addTaskUseCase(task)

            // Then
            coVerify {
                tasksRepository.addTask(match {
                    it.id == taskId &&
                    it.title == "Test Task" &&
                    it.projectId == projectId &&
                    it.description == "Test description" &&
                    it.stateId == stateId
                })
            }
        }
    }