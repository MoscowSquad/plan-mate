package utilities.csv_parser

import org.junit.jupiter.api.BeforeEach

class ProjectCsvParserTest {
    private lateinit var parser: ProjectCsvParser

    @BeforeEach
    fun setUp() {
        parser = ProjectCsvParser()
    }
}