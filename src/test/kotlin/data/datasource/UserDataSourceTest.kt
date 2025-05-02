package data.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Role
import logic.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.csv_parser.CsvHandler
import data.csv_parser.UserCsvParser
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
            User(UUID.randomUUID(), "Aiman", "123456", Role.ADMIN),
            User(UUID.randomUUID(), "Muhammed", "123456", Role.MATE),
            User(UUID.randomUUID(), "Zeyad", "123456", Role.MATE),
            User(UUID.randomUUID(), "Yaser", "123456", Role.MATE),
        )
        every { userCsvParser.parse(any()) } returns users

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `fetch() should return empty list when there is no users returned by user csv-parser`() {
        val users = listOf<User>()
        every { userCsvParser.parse(any()) } returns users

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(users)
    }

    @Test
    fun `save() should call CsvHandler to parse when save users data`() {
        dataSource.save(emptyList())
        verify { csvHandler.write(any(), any()) }
    }
}