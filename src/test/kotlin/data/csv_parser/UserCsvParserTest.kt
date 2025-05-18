package data.csv_parser

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.UserCsvParser
import data.csv_data.dto.UserDto
import data.csv_data.util.MATE
import test_helper.createUser
import test_helper.getUsers
import test_helper.getUsersCsvLines
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserCsvParserTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var parser: UserCsvParser

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        parser = UserCsvParser()
    }

    @Test
    fun `should return users when parse data from user file`() {
        // Given
        val csvLines = getUsersCsvLines()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

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
        val result = parser.parse(csvLines)

        // Then
        val users = emptyList<UserDto>()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should return empty list when parse data from empty user file with csv-header`() {
        // Given
        val csvLines = listOf("id,name,hashedPassword,role,projectIds")
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse(csvLines)

        // Then
        val users = emptyList<UserDto>()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should return csv-data when serialize user data`() {
        // Given
        val users = getUsers()

        // When
        val result = parser.serialize(users)

        // Then
        val csvLines = getUsersCsvLines()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return user header when serialize empty user data`() {
        // Given
        val users = emptyList<UserDto>()

        // When
        val result = parser.serialize(users)

        // Then
        val csvLines = listOf("id,name,hashedPassword,role,projectIds")
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return empty list when serialize and parse user with empty project ids string`() {
        // Given
        val user = createUser("test-id", "TestUser", "password123", MATE, emptyList(), emptyList())
        val serialized =
            listOf("id,name,hashedPassword,role,projectIds,taskIds", "test-id,TestUser,password123,${MATE},[],[]")

        // When
        val parsedResult = parser.parse(serialized)

        // Then
        Truth.assertThat(parsedResult).containsExactly(user)
    }

    @Test
    fun `should handle blank project ids when parsing`() {
        // Given
        val csvLines = listOf(
            "id,name,hashedPassword,role,projectIds",
            "test-id,TestUser,password123,${MATE},[, ,  ]"
        )

        // When
        val result = parser.parse(csvLines)

        // Then
        Truth.assertThat(result).containsExactly(
            createUser("test-id", "TestUser", "password123", MATE, emptyList(), emptyList())
        )
    }

    @Test
    fun `should return empty list when parsing blank project ids`() {
        // Given
        val csvLines = listOf(
            "id,name,hashedPassword,role,projectIds",
            "test-id,TestUser,password123,${MATE},"
        )

        // When
        val result = parser.parse(csvLines)

        // Then
        Truth.assertThat(result).containsExactly(
            createUser("test-id", "TestUser", "password123", MATE, emptyList(), emptyList())
        )
    }
}