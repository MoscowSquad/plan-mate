package test_helper

fun List<String>.toCsvData(): List<String> {
    val data: MutableList<String> = mutableListOf()
    this.forEach {
        data += it
    }
    return data
}