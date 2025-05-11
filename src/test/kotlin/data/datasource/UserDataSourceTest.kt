package data.datasource

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.UserCsvParser
import data.csv_data.datasource.UserDataSource
import data.csv_data.dto.UserDto
import data.csv_data.util.ADMIN
import data.csv_data.util.MATE
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserDataSourceTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var userCsvParser: UserCsvParser

    private lateinit var dataSource: UserDataSource

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        userCsvParser = mockk(relaxed = true)

        dataSource = UserDataSource(csvHandler, userCsvParser)
    }

    @Test
    fun `fetch() should call UserCsvParser to parse when return users data`() {
        // Given
        val csvLines = listOf("id,name,password,role,projectsIds", "1,User1,password,ADMIN,")
        every { csvHandler.getLines() } returns csvLines

        // When
        dataSource.fetch()

        // Then
        verify { csvHandler.getLines() }
        verify { userCsvParser.parse(csvLines) }
    }

    @Test
    fun `fetch() should return parsed users when there is users returned by user csv-parser`() {
        // Given
        val users = listOf(
            UserDto(UUID.randomUUID().toString(), "Aiman", "123456", ADMIN, emptyList()),
            UserDto(UUID.randomUUID().toString(), "Muhammed", "123456", MATE, listOf(UUID.randomUUID().toString())),
            UserDto(
                UUID.randomUUID().toString(),
                "Zeyad",
                "123456",
                MATE,
                listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())
            ),
            UserDto(UUID.randomUUID().toString(), "Yaser", "123456", MATE, emptyList()),
        )
        every { csvHandler.getLines() } returns listOf("id,name,password,role,projectsIds")
        every { userCsvParser.parse(any()) } returns users

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `fetch() should return empty list when there is no users returned by user csv-parser`() {
        // Given
        val users = listOf<UserDto>()
        every { csvHandler.getLines() } returns listOf("id,name,password,role,projectsIds")
        every { userCsvParser.parse(any()) } returns users

        // When
        val result = dataSource.fetch()

        // Then
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `save() should call CsvParser to serialize and CsvHandler to write when saving user data`() {
        // Given
        val users = listOf(UserDto("1", "User1", "password", ADMIN, emptyList()))
        val serializedData = listOf("id,name,password,role,projectsIds", "1,User1,password,ADMIN,")
        every { userCsvParser.serialize(users) } returns serializedData

        // When
        dataSource.save(users)

        // Then
        verify { userCsvParser.serialize(users) }
        verify { csvHandler.write(serializedData) }
    }
}