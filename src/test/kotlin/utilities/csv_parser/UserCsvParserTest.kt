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
    private lateinit var csvReader: UserCsvReader
    private lateinit var csvWriter: UserCsvWriter
    private lateinit var parser: UserCsvParser

    @BeforeEach
    fun setUp() {
        csvReader = mockk(relaxed = true)
        csvWriter = mockk(relaxed = true)
        parser = UserCsvParser(csvReader, csvWriter)
    }

    @Test
    fun `should call UserCsvReader when parsing users data`() {
        parser.parse()
        verify { csvReader.getLines() }
    }

    @Test
    fun `should call UserCsvWriter when serialize users data`() {
        parser.serialize(emptyList())
        verify { csvWriter.write("") }
    }


    @Test
    fun `should return users when parse data from user file`() {
        // Given
        val csvLines = getCsvLines()
        every { csvReader.getLines() } returns csvLines

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
        every { csvReader.getLines() } returns csvLines

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
        every { csvReader.getLines() } returns csvLines

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
            "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,Video 101 to 120,,w09w98we-d23d-4f69-b6f7-3336b68f2ae4,w09w98we-d23d-4f69-b6f7-3336b68f2ae4",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Video 201 to 220,Don't watch video N0.203,8223k433-3l3l-23j0-b6f7-3336b68f2ae4,lk342l33-3l3l-0923-23l0-23k0i32k3303",
            "07f641d4-077e-4f08-978d-3b6c9587f4bf,Video 301 to 320,,9283h32p-o320-lk30-b6f7-3336b68f2ae4,ki33h32p-3ij3-lk30-k303-k3i03j39j030",
        )
    }

    private fun createUser(
        id: String,
        userName: String,
        hashedPassword: String,
        roleCode: Int,
    ): User {
        val role = when (roleCode) {
            1 -> Role.ADMIN
            2 -> Role.MATE
            else -> throw Exception("There is no role with this code{$roleCode}")
        }
        return User(UUID.fromString(id), userName, hashedPassword, role)
    }
}