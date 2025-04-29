package utilities.csv_parser

import org.junit.jupiter.api.BeforeEach

class AuditLogCsvParserTest {
    private lateinit var parser: AuditLogCsvParser

    @BeforeEach
    fun setUp() {
        parser = AuditLogCsvParser()
    }
}