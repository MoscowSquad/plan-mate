package data.datasource

import com.google.common.truth.Truth
import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.UserCsvParser
import data.csv_data.datasource.UserDataSource
import data.csv_data.util.ADMIN
import data.csv_data.util.MATE
import data.mongodb_data.dto.UserDto
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
        dataSource.fetch()
        verify { userCsvParser.parse(any()) }
    }

    @Test
    fun `fetch() should return parsed users when there is users returned by user csv-parser`() {
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
        every { userCsvParser.parse(any()) } returns users

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `fetch() should return empty list when there is no users returned by user csv-parser`() {
        val users = listOf<UserDto>()
        every { userCsvParser.parse(any()) } returns users

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `save() should call CsvHandler to parse when save users data`() {
        dataSource.save(emptyList())
        verify { csvHandler.write(any()) }
    }
}