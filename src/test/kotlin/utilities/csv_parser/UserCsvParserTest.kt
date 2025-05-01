package utilities.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Role
import logic.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test_helper.toCsvData
import java.util.*

class UserCsvParserTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var parser: UserCsvParser

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        parser = UserCsvParser(csvHandler)
    }

    @Test
    fun `should call UserCsvHandler when parsing users data`() {
        parser.parse()
        verify { csvHandler.getLines() }
    }

    @Test
    fun `should call UserCsvHandler when serialize users data`() {
        parser.serialize(emptyList())
        verify { csvHandler.write(emptyList()) }
    }


    @Test
    fun `should return users when parse data from user file`() {
        // Given
        val csvLines = getCsvLines()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val users = getUsers()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should return empty list when parse data from empty user file`() {
        // Given
        val csvLines = listOf<String>()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val users = emptyList<User>()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should return empty list when parse data from empty user file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,username,hashedPassword,role",
        )
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val users = emptyList<User>()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should return csv-data when serialize user data`() {
        // Given
        val users = getUsers()

        // When
        val result = parser.serialize(users)

        // Then
        val csvLines = getCsvLines().toCsvData()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return user header when serialize empty user data`() {
        // Given
        val users = emptyList<User>()

        // When
        val result = parser.serialize(users)

        // Then
        val csvLines = "id,username,hashedPassword,role"
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getUsers(): List<User> {
        return listOf(
            createUser("82e16049-a9fb-4f69-b6f7-3336b68f2ae4", "Aiman", "3336b68f2ae4", Role.ADMIN.value),
            createUser("045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", "Zeyad", "09sd23od3skd", Role.ADMIN.value),
            createUser("07f641d4-077e-4f08-978d-3b6c9587f4bf", "Yaser", "wk5dr98sd6dd", Role.MATE.value),
        )
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,username,hashedPassword,role",
            "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,Aiman,3336b68f2ae4,Admin",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Zeyad,09sd23od3skd,Admin",
            "07f641d4-077e-4f08-978d-3b6c9587f4bf,Yaser,wk5dr98sd6dd,Mate",
        )
    }

    private fun createUser(
        id: String,
        userName: String,
        hashedPassword: String,
        roleCode: String,
    ): User {
        val role = when (roleCode) {
            "Admin" -> Role.ADMIN
            "Mate" -> Role.MATE
            else -> throw Exception("There is no role with this code{$roleCode}")
        }
        return User(UUID.fromString(id), userName, hashedPassword, role)
    }
}