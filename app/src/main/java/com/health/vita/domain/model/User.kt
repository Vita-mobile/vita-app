package com.health.vita.domain.model

data class User (
    var id: String = "",
    var name: String = "",
    var lastName: String = "",
    var email: String = "",
    var age: Int = 0,
    var weight: Float = 0.0f,
    var height: Float = 0.0f,
    var physicalLevel: Int = 0,
    var sex: String = "",
    var physicalTarget: String = ""

)