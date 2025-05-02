package test_helper

fun List<String>.toCsvData(): List<String> {
    var data: MutableList<String> = mutableListOf()
    this.forEach {
        data += it
    }
    return data
}