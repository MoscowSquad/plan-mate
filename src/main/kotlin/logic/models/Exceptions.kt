package logic.models

sealed class Exceptions(message: String) : Exception(message) {

    class UnauthorizedException(message: String) : Exception(message)
}