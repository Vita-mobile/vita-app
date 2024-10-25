package com.health.vita.domain.model

data class User (
    var id: String = "",
    var name: String = "",
    var lastName: String = "",
    var email: String = "",
    var weight: Float = 0.0f,
    var age: Int = 0,
    var height: Float = 0.0f,
    var gender: String = "",
    var goal: String = "",
    var activityLevel: Int = 0
)