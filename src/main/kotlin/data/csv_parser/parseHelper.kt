package data.csv_parser

import kotlinx.datetime.LocalDateTime
import java.util.UUID

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}

fun String.toTimeStamp(): LocalDateTime {
    return LocalDateTime.parse(this)
}