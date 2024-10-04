package com.health.vita.core.utils.error_management

open class AppError(

    val message: String,
    val cause: Throwable? = null
)

class NetworkError(

    message: String =  "Network error.",
    cause: Throwable? = null
) : AppError(message, cause )


class DatabaseError(
    message: String = "Database error.",
    cause: Throwable? = null
) : AppError(message, cause)

class UnknownError(
    message: String = "Unknown error.",
    cause: Throwable? = null
) : AppError(message, cause)