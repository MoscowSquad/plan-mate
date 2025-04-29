package utilities.csv_parser

import org.junit.jupiter.api.BeforeEach

class StateCsvParserTest {
    private lateinit var parser: StateCsvParser

    @BeforeEach
    fun setUp() {
        parser = StateCsvParser()
    }
}