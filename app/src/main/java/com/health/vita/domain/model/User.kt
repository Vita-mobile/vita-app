package com.health.vita.domain.model

data class User (
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var weight: Double = 0.0,
    var age: Int = 0,
    var height: Double = 0.0,
    var gender: String = "",
    var goal: String = "",
    var activityLevel: Int = 0
)