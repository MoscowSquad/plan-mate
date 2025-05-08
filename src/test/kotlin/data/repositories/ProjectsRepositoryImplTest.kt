package data.repositories

    import com.google.common.truth.Truth.assertThat
    import data.csv_parser.CsvHandler
    import data.csv_parser.ProjectCsvParser
    import data.datasource.ProjectDataSource
    import io.mockk.every
    import io.mockk.mockk
    import io.mockk.verify
    import logic.models.Project
    import logic.util.ProjectNotFoundException
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import org.junit.jupiter.api.assertThrows
    import java.util.*

class ProjectsRepositoryImplTest {

        private lateinit var csvHandler: CsvHandler
        private lateinit var csvParser: ProjectCsvParser
        private lateinit var dataSource: ProjectDataSource
        private lateinit var projectsRepository: ProjectsRepositoryImpl
        private lateinit var testProject1: Project
        private lateinit var testProject2: Project
        private lateinit var testProject3: Project

        @BeforeEach
        fun setUp() {
            csvHandler = mockk(relaxed = true)
            csvParser = mockk(relaxed = true)
            dataSource = ProjectDataSource(csvHandler, csvParser)
            projectsRepository = ProjectsRepositoryImpl(dataSource)

            // Create test projects with fixed UUIDs for consistency
            testProject1 = Project(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Project 1")
            testProject2 = Project(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Project 2")
            testProject3 = Project(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Project 3")
        }

        @Test
        fun `add should store project in repository`() {
            // When
            val result = projectsRepository.addProject(testProject1)

            // Then
            assertThat(result).isTrue()
            assertThat(projectsRepository.getAllProjects()).containsExactly(testProject1)
        }

        @Test
        fun `add should handle multiple projects`() {
            // When
            projectsRepository.addProject(testProject1)
            projectsRepository.addProject(testProject2)

            // Then
            assertThat(projectsRepository.getAllProjects()).containsExactly(testProject1, testProject2)
        }

        @Test
        fun `update should modify existing project`() {
            // Given
            projectsRepository.addProject(testProject1)
            val updatedProject = Project(testProject1.id, "Updated Project")

            // When
            val result = projectsRepository.updateProject(updatedProject)

            // Then
            assertThat(result).isTrue()
            assertThat(projectsRepository.getProjectById(testProject1.id)).isEqualTo(updatedProject)
        }

        @Test
        fun `update should return false when project does not exist`() {
            // Given
            val nonExistingProject = Project(UUID.randomUUID(), "Non-existing")

            // When
            val result = projectsRepository.updateProject(nonExistingProject)

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `delete should remove project from repository`() {
            // Given
            projectsRepository.addProject(testProject1)
            projectsRepository.addProject(testProject2)

            // When
            val result = projectsRepository.deleteProject(testProject1.id)

            // Then
            assertThat(result).isTrue()
            assertThat(projectsRepository.getAllProjects()).containsExactly(testProject2)
        }

        @Test
        fun `delete should return false when project does not exist`() {
            // When
            val result = projectsRepository.deleteProject(UUID.randomUUID())

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `getAll should return empty list when repository is empty`() {
            // When
            val result = projectsRepository.getAllProjects()

            // Then
            assertThat(result).isEmpty()
        }

        @Test
        fun `getAll should return all projects in repository`() {
            // Given
            projectsRepository.addProject(testProject1)
            projectsRepository.addProject(testProject2)
            projectsRepository.addProject(testProject3)

            // When
            val result = projectsRepository.getAllProjects()

            // Then
            assertThat(result).containsExactly(testProject1, testProject2, testProject3)
        }

        @Test
        fun `getById should return project when it exists`() {
            // Given
            projectsRepository.addProject(testProject1)
            projectsRepository.addProject(testProject2)

            // When
            val result = projectsRepository.getProjectById(testProject2.id)

            // Then
            assertThat(result).isEqualTo(testProject2)
        }

        @Test
        fun `getById should throw ProjectNotFoundException when project does not exist`() {
            // Given
            val nonExistingId = UUID.randomUUID()

            // When & Then
            val exception = assertThrows<ProjectNotFoundException> {
                projectsRepository.getProjectById(nonExistingId)
            }

            assertThat(exception.message).contains(nonExistingId.toString())
        }

        @Test
        fun `getAll should return a copy of the internal list`() {
            // Given
            projectsRepository.addProject(testProject1)

            // When - modify the returned list
            val projects = projectsRepository.getAllProjects()
            projects.toMutableList().add(testProject2)

            // Then - internal repository should be unchanged
            assertThat(projectsRepository.getAllProjects()).containsExactly(testProject1)
        }

        @Test
        fun `add should save to data source when project is added`() {
            // Given
            val mockDataSource = mockk<ProjectDataSource>(relaxed = true)
            projectsRepository = ProjectsRepositoryImpl(mockDataSource)

            // When
            val result = projectsRepository.addProject(testProject1)

            // Then
            assertThat(result).isTrue()
            verify(exactly = 1) { mockDataSource.save(any()) }
        }

        @Test
        fun `add should not save to data source when project cannot be added`() {
            // Given
            val mockDataSource = mockk<ProjectDataSource>(relaxed = true)
            val mockedProjects = mockk<MutableList<Project>> {
                every { add(any()) } returns false
            }
            // Use reflection to replace the projects field with our mock
            val projectsField = ProjectsRepositoryImpl::class.java.getDeclaredField("projects")
            projectsField.isAccessible = true

            projectsRepository = ProjectsRepositoryImpl(mockDataSource)
            projectsField.set(projectsRepository, mockedProjects)

            // When
            val result = projectsRepository.addProject(testProject1)

            // Then
            assertThat(result).isFalse()
            verify(exactly = 0) { mockDataSource.save(any()) }
        }
    }