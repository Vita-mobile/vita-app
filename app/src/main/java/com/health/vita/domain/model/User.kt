package com.health.vita.domain.model

import androidx.compose.ui.text.font.FontWeight

data class User(
    var id:String = "",
    var name:String = "",
    var email:String = "",
    var image:String = "",
    var weight: Float = 0f,
    var age:Int = 0,
    var height:Float = 0f,
    var gender:String = "",
    var goal: String = "",
    var activityLevel: String = ""
    )