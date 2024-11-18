package com.health.vita.core.navigation

object Screen {
    // Main
    const val HOME = "home"
    const val SPLASH_SCREEN = "SplashScreen"
    const val WELCOME_SCREEN = "WelcomeScreen"
    const val ACCOUNT_SETTINGS = "AccountSettings"
    const val LOAD_SIMULATION = "LoadSimulationScreen"

    // Auth
    const val LOGIN = "LogIn"
    //Signup
    const val SIGN_UP = "SignUp"
    const val SELECT_AVATAR = "SelectAvatar"
    const val WEIGHT_SELECTION = "WeightSelection"
    const val HEIGHT_SELECTION = "HeightSelection"
    const val AGE_SELECTION = "AgeSelection"
    const val FITNESS_LEVEL_SELECTION = "FitnessLevelSelection"
    const val SEX_SELECTION = "SexSelection"
    const val FITNESS_GOAL_SELECTION = "FitnessGoalSelection"

    //Utils
    const val RESET_PASSWORD = "ResetPassword"
    const val RESET_PASSWORD_CONFIRMATION = "ResetPasswordConfirmation"



    // Profile
    const val PROFILE = "Profile"
    const val NOTIFICATIONS = "Notifications"
    const val PROFILE_EDITION = "ProfileEdition"
    const val EDIT_WEIGHT_SELECTION = "EditWeight"
    const val EDIT_HEIGHT_SELECTION = "EditHeight"


    // Meals
    const val MEAL_HOME = "MealHome"
    const val DIET_SELECTION = "DietSelection"
    const val MEAL_TRACKING = "MealTracking"
    const val HYDRATION = "Hydration"
    const val DIETS_PREVIEW = "DietsPreview/{meal}"
    const val ADDED_FOOD = "AddedFood"
    const val NUTRITION_WELCOME = "NutritionWelcome"
    const val MEAL_DETAIL = "MealDetail/{meal}/{isFavorite}"
    const val CREATE_MEAL = "CreateMeal"

    // Sports
    const val WORKOUT_HOME = "WorkoutHome"
    const val SCHEDULE_SELECTION = "ScheduleSelection"
    const val SPORT_PREFERENCE_SELECTION = "SportPreferenceSelection"
    const val WEEK_WORKOUT = "WeekWorkout"
    const val WORKOUT_PREVIEW = "WorkoutPreview"
    const val WORKOUT_DETAIL = "WorkoutDetail"
    const val WORKOUT_ENDING = "WorkoutEnding"
    const val DAY_WORKOUT = "DayWorkout"
    const val EXPRESS_WORKOUT_SELECTION = "ExpressWorkoutSelection"
    const val EXPRESS_TIME_SETTING = "ExpressTimeSetting"
    const val EXPRESS_PREFERENCE = "ExpressPreference"
    const val FULFILLED_EXERCISE = "FulfilledExercise"
    const val FULFILLED_ACTIVITY = "FulfilledActivity"
    const val FULFILLED_CARDIO_INTENSITY = "FulfilledCardioIntensity"
    const val FULFILLED_STRENGTH_INTENSITY = "FulfilledStrengthIntensity"
    const val REGISTERED_ACTIVITY = "RegisteredActivity"
}

/**
 * ----------- Main -----------
 * Splash Screen
 * Welcome Screen
 * Home
 * AccountSettings
 * ----------- Auth -----------
 * LogIn
 * SignUp
 * ResetPassword (Opt)
 * ResetPasswordConfirmation (Opt)
 * SelectAvatar (Opt)
 * ----------- Profile -----------
 * Profile
 * Notifications
 * ProfileEdition
 * WeightSelection
 * HeightSelection
 * AgeSelection
 * FitnessLevelSelection
 * SexSelection
 * SexSelection
 * FitnessGoalSelection
 * ----------- Meals -----------
 * MealHome
 * DietSelection
 * MealTracking
 * Hydration
 * DietsPreview
 * AddedFood
 * NutritionWelcome
 * MealDetail
 * ----------- Sports -----------
 * WorkoutHome
 * ScheduleSelection
 * SportPreferenceSelection
 * WeekWorkout
 * WorkoutPreview
 * WorkoutDetail
 * WorkoutEnding
 * DayWorkout
 * ExpressWorkoutSelection
 * ExpressTimeSetting
 * ExpressPreference
 * FulfilledExercise
 * FulfilledActivity
 * FulfilledCardioIntensity
 * FulfilledStrengthIntensity
 * RegisteredActivity
 * */