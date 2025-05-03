package data.csv_parser

import com.google.common.truth.Truth
import data.dto.UserDto
import data.util.ADMIN
import data.util.MATE
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
        val csvLines = getCsvLines()
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
        val csvLines = listOf(
            "id,name,hashedPassword,role,projectIds",
        )
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
        val csvLines = getCsvLines()
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


    private fun getUsers(): List<UserDto> {
        return listOf(
            createUser("82e16049-a9fb-4f69-b6f7-3336b68f2ae4", "Aiman", "3336b68f2ae4", ADMIN, emptyList()),
            createUser(
                "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f",
                "Zeyad",
                "09sd23od3skd",
                ADMIN,
                listOf("82e16049-a9fb-4f69-b6f7-3336b68f2ae4")
            ),
            createUser(
                "07f641d4-077e-4f08-978d-3b6c9587f4bf",
                "Yaser",
                "wk5dr98sd6dd",
                MATE,
                listOf("82e16049-a9fb-4f69-b6f7-3336b68f2ae4", "82e16049-a9fb-4f69-b6f7-3336b68f2ae4")
            ),
        )
    }

    private fun getCsvLines(): List<String> {
        return listOf(
            "id,name,hashedPassword,role,projectIds",
            "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,Aiman,3336b68f2ae4,${ADMIN},[]",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,Zeyad,09sd23od3skd,${ADMIN},[82e16049-a9fb-4f69-b6f7-3336b68f2ae4]",
            "07f641d4-077e-4f08-978d-3b6c9587f4bf,Yaser,wk5dr98sd6dd,${MATE},[82e16049-a9fb-4f69-b6f7-3336b68f2ae4, 82e16049-a9fb-4f69-b6f7-3336b68f2ae4]",
        )
    }

    private fun createUser(
        id: String,
        userName: String,
        hashedPassword: String,
        role: String,
        projectIds: List<String>
    ): UserDto {
        return UserDto(id, userName, hashedPassword, role, projectIds)
    }
}