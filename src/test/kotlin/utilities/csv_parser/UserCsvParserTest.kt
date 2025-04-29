package utilities.csv_parser

import org.junit.jupiter.api.BeforeEach

class UserCsvParserTest {
    private lateinit var parser: UserCsvParser

    @BeforeEach
    fun setUp() {
        parser = UserCsvParser()
    }
}