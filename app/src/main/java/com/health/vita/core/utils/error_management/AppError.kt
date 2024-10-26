package com.health.vita.core.utils.error_management

open class AppError(

    val message: String  ="An error occurred",
    val cause: Throwable? = null
)

class NetworkError(
    message: String =  "Error con la conexión.",
    cause: Throwable? = null
) : AppError(message, cause )

class DatabaseError(
    message: String = "Error interno con la base de datos.",
    cause: Throwable? = null
) : AppError(message, cause)

class UnknownError(
    message: String = "Error desconocido, el error será reportado.",
    cause: Throwable? = null
) : AppError(message, cause)


class AuthCredentialsError(
    message: String = "Error al definir las credenciales.",
    cause: Throwable? = null
) : AppError(message, cause)

class FirebaseError(
    message: String = "Error al usar firebase.",
    cause: Throwable? = null
) : AppError(message, cause)