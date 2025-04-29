package utilities.csv_parser

import org.junit.jupiter.api.BeforeEach

class TaskCsvParserTest {
    private lateinit var parser: TaskCsvParser

    @BeforeEach
    fun setUp() {
        parser = TaskCsvParser()
    }
}