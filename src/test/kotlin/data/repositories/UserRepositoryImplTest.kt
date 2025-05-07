package data.repositories

        import data.csv_parser.CsvHandler
        import data.csv_parser.UserCsvParser
        import data.datasource.UserDataSource
        import data.dto.UserDto
        import io.mockk.every
        import io.mockk.mockk
        import io.mockk.verify
        import logic.models.User
        import logic.models.UserRole
        import org.junit.jupiter.api.Assertions.*
        import org.junit.jupiter.api.BeforeEach
        import org.junit.jupiter.api.Test
        import org.junit.jupiter.api.assertThrows
        import java.util.*

        class UserRepositoryImplTest {

            private lateinit var csvHandler: CsvHandler
            private lateinit var csvParser: UserCsvParser
            private lateinit var dataSource: UserDataSource
            private lateinit var repository: UserRepositoryImpl
            private lateinit var user: User

            @BeforeEach
            fun setUp() {
                csvHandler = mockk(relaxed = true)
                csvParser = mockk(relaxed = true)
                dataSource = UserDataSource(csvHandler, csvParser)
                repository = UserRepositoryImpl(dataSource)
                user = User(
                    id = UUID.randomUUID(),
                    name = "Test User",
                    hashedPassword = "hashed",
                    role = UserRole.MATE,
                    projectIds = listOf()
                )
            }

            @Test
            fun `add user successfully`() {
                val result = repository.addUser(user)
                assertTrue(result)
                assertEquals(user, repository.getUserById(user.id))
            }

            @Test
            fun `add user with duplicate ID throws exception`() {
                repository.addUser(user)
                val duplicateUser = user.copy(name = "Duplicate")
                val exception = assertThrows<IllegalArgumentException> {
                    repository.addUser(duplicateUser)
                }
                assertEquals("User with id ${user.id} already exists", exception.message)
            }

            @Test
            fun `delete existing user successfully`() {
                repository.addUser(user)
                val result = repository.deleteUser(user.id)
                assertTrue(result)
            }

            @Test
            fun `delete non-existing user throws exception`() {
                val fakeId = UUID.randomUUID()
                val exception = assertThrows<NoSuchElementException> {
                    repository.deleteUser(fakeId)
                }
                assertEquals("Cannot delete: User with id $fakeId not found", exception.message)
            }

            @Test
            fun `assign user to project successfully`() {
                repository.addUser(user)
                val projectId = UUID.randomUUID()
                val result = repository.assignUserToProject(projectId, user.id)
                assertTrue(result)
                assertTrue(repository.getUserById(user.id).projectIds.contains(projectId))
            }

            @Test
            fun `assign user to already assigned project throws exception`() {
                repository.addUser(user)
                val projectId = UUID.randomUUID()
                repository.assignUserToProject(projectId, user.id)
                val exception = assertThrows<IllegalStateException> {
                    repository.assignUserToProject(projectId, user.id)
                }
                assertEquals("Project $projectId is already assigned to user ${user.id}", exception.message)
            }

            @Test
            fun `assign project to non-existing user throws exception`() {
                val fakeId = UUID.randomUUID()
                val projectId = UUID.randomUUID()
                val exception = assertThrows<NoSuchElementException> {
                    repository.assignUserToProject(projectId, fakeId)
                }
                assertEquals("User with id $fakeId not found", exception.message)
            }

            @Test
            fun `revoke user from project successfully`() {
                val projectId = UUID.randomUUID()
                repository.addUser(user)
                repository.assignUserToProject(projectId, user.id)
                val result = repository.unassignUserFromProject(projectId, user.id)
                assertTrue(result)
                assertFalse(repository.getUserById(user.id).projectIds.contains(projectId))
            }

            @Test
            fun `revoke project not assigned throws exception`() {
                val projectId = UUID.randomUUID()
                repository.addUser(user)
                val exception = assertThrows<IllegalStateException> {
                    repository.unassignUserFromProject(projectId, user.id)
                }
                assertEquals("Project $projectId is not assigned to user ${user.id}", exception.message)
            }

            @Test
            fun `revoke from non-existing user throws exception`() {
                val fakeId = UUID.randomUUID()
                val projectId = UUID.randomUUID()
                val exception = assertThrows<NoSuchElementException> {
                    repository.unassignUserFromProject(projectId, fakeId)
                }
                assertEquals("User with id $fakeId not found", exception.message)
            }

            @Test
            fun `getById returns user when found`() {
                repository.addUser(user)
                val result = repository.getUserById(user.id)
                assertEquals(user.id, result.id)
            }

            @Test
            fun `getById returns correct user from multiple users`() {
                val user2 = user.copy(id = UUID.randomUUID(), name = "User2")
                val user3 = user.copy(id = UUID.randomUUID(), name = "User3")
                repository.addUser(user)
                repository.addUser(user2)
                repository.addUser(user3)

                val result = repository.getUserById(user2.id)
                assertEquals(user2.id, result.id)
                assertEquals("User2", result.name)
            }

            @Test
            fun `getById throws exception when user not found`() {
                val fakeId = UUID.randomUUID()
                val exception = assertThrows<NoSuchElementException> {
                    repository.getUserById(fakeId)
                }
                assertEquals("User with id $fakeId not found", exception.message)
            }

            @Test
            fun `getAll returns all users`() {
                val user2 = user.copy(id = UUID.randomUUID(), name = "Second")
                repository.addUser(user)
                repository.addUser(user2)
                val allUsers = repository.getAllUsers()
                assertEquals(2, allUsers.size)
                assertTrue(allUsers.contains(user))
                assertTrue(allUsers.contains(user2))
            }

            @Test
            fun `getAllUsers loads data from data source when users list is empty`() {
                repository = UserRepositoryImpl(dataSource) // Fresh repository with empty users list

                val userData = listOf(user.copy(), user.copy(id = UUID.randomUUID(), name = "Other User"))
                every { dataSource.fetch() } returns userData.map { UserDto(it.id.toString(), it.name, it.hashedPassword, it.role.toString(), it.projectIds.map { id -> id.toString() }) }
                val result = repository.getAllUsers()

                verify { dataSource.fetch() }
                assertEquals(2, result.size)
            }

            @Test
            fun `getAllUsers returns cached data when users list is not empty`() {
                repository.addUser(user)

                repository.getAllUsers()

                verify(exactly = 0) { dataSource.fetch() }
            }
        }