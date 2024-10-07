package com.health.vita.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.health.vita.auth.presentation.login.LoginScreen
import com.health.vita.auth.presentation.ResetPasswordConfirmationScreen
import com.health.vita.auth.presentation.ResetPasswordScreen
import com.health.vita.auth.presentation.SelectAvatarScreen
import com.health.vita.auth.presentation.SignUpScreen
import com.health.vita.main.presentation.AccountSettingsScreen
import com.health.vita.main.presentation.HomeScreen
import com.health.vita.main.presentation.SplashScreen
import com.health.vita.main.presentation.WelcomeScreen
import com.health.vita.meals.presentation.AddedFoodScreen
import com.health.vita.meals.presentation.DietSelectionScreen
import com.health.vita.meals.presentation.DietsPreviewScreen
import com.health.vita.meals.presentation.HydrationScreen
import com.health.vita.meals.presentation.MealDetailScreen
import com.health.vita.meals.presentation.MealHomeScreen
import com.health.vita.meals.presentation.MealTrackingScreen
import com.health.vita.meals.presentation.NutritionWelcomeScreen
import com.health.vita.profile.presentation.AgeSelectionScreen
import com.health.vita.profile.presentation.FitnessGoalSelectionScreen
import com.health.vita.profile.presentation.FitnessLevelSelectionScreen
import com.health.vita.profile.presentation.HeightSelectionScreen
import com.health.vita.profile.presentation.NotificationsScreen
import com.health.vita.profile.presentation.ProfileEditionScreen
import com.health.vita.profile.presentation.ProfileScreen
import com.health.vita.profile.presentation.SexSelectionScreen
import com.health.vita.profile.presentation.WeightSelectionScreen
import com.health.vita.sports.presentation.DayWorkoutScreen
import com.health.vita.sports.presentation.ExpressPreferenceScreen
import com.health.vita.sports.presentation.ExpressTimeSettingScreen
import com.health.vita.sports.presentation.ExpressWorkoutSelectionScreen
import com.health.vita.sports.presentation.FulfilledActivityScreen
import com.health.vita.sports.presentation.FulfilledCardioIntensityScreen
import com.health.vita.sports.presentation.FulfilledExerciseScreen
import com.health.vita.sports.presentation.FulfilledStrengthIntensityScreen
import com.health.vita.sports.presentation.RegisteredActivityScreen
import com.health.vita.sports.presentation.ScheduleSelectionScreen
import com.health.vita.sports.presentation.SportPreferenceSelectionScreen
import com.health.vita.sports.presentation.WeekWorkoutScreen
import com.health.vita.sports.presentation.WorkoutDetailScreen
import com.health.vita.sports.presentation.WorkoutEndingScreen
import com.health.vita.sports.presentation.WorkoutHomeScreen
import com.health.vita.sports.presentation.WorkoutPreviewScreen


@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.LOGIN
    ) {
        // Main
        composable(Screen.HOME) {
            HomeScreen(navController)
        }
        composable(Screen.SPLASH_SCREEN) {
            SplashScreen(navController)
        }
        composable(Screen.WELCOME_SCREEN) {
            WelcomeScreen(navController)
        }
        composable(Screen.ACCOUNT_SETTINGS) {
            AccountSettingsScreen(navController)
        }

        // Auth
        composable(Screen.LOGIN) {
            LoginScreen(navController)
        }
        composable(Screen.SIGN_UP) {
            SignUpScreen(navController)
        }
        composable(Screen.RESET_PASSWORD) {
            ResetPasswordScreen(navController)
        }
        composable(Screen.RESET_PASSWORD_CONFIRMATION) {
            ResetPasswordConfirmationScreen(navController)
        }
        composable(Screen.SELECT_AVATAR) {
            SelectAvatarScreen(navController)
        }

        // Profile
        composable(Screen.PROFILE) {
            ProfileScreen(navController)
        }
        composable(Screen.NOTIFICATIONS) {
            NotificationsScreen(navController)
        }
        composable(Screen.PROFILE_EDITION) {
            ProfileEditionScreen(navController)
        }
        composable(Screen.WEIGHT_SELECTION) {
            WeightSelectionScreen(navController)
        }
        composable(Screen.HEIGHT_SELECTION) {
            HeightSelectionScreen(navController)
        }
        composable(Screen.AGE_SELECTION) {
            AgeSelectionScreen(navController)
        }
        composable(Screen.FITNESS_LEVEL_SELECTION) {
            FitnessLevelSelectionScreen(navController)
        }
        composable(Screen.SEX_SELECTION) {
            SexSelectionScreen(navController)
        }
        composable(Screen.FITNESS_GOAL_SELECTION) {
            FitnessGoalSelectionScreen(navController)
        }

        // Meals
        composable(Screen.MEAL_HOME) {
            MealHomeScreen(navController)
        }
        composable(Screen.DIET_SELECTION) {
            DietSelectionScreen(navController)
        }
        composable(Screen.MEAL_TRACKING) {
            MealTrackingScreen(navController)
        }
        composable(Screen.HYDRATION) {
            HydrationScreen(navController)
        }
        composable(Screen.DIETS_PREVIEW) {
            DietsPreviewScreen(navController)
        }
        composable(Screen.ADDED_FOOD) {
            AddedFoodScreen(navController)
        }
        composable(Screen.NUTRITION_WELCOME) {
            NutritionWelcomeScreen(navController)
        }
        composable(Screen.MEAL_DETAIL) {
            MealDetailScreen(navController)
        }

        // Sports
        composable(Screen.WORKOUT_HOME) {
            WorkoutHomeScreen(navController)
        }
        composable(Screen.SCHEDULE_SELECTION) {
            ScheduleSelectionScreen(navController)
        }
        composable(Screen.SPORT_PREFERENCE_SELECTION) {
            SportPreferenceSelectionScreen(navController)
        }
        composable(Screen.WEEK_WORKOUT) {
            WeekWorkoutScreen(navController)
        }
        composable(Screen.WORKOUT_PREVIEW) {
            WorkoutPreviewScreen(navController)
        }
        composable(Screen.WORKOUT_DETAIL) {
            WorkoutDetailScreen(navController)
        }
        composable(Screen.WORKOUT_ENDING) {
            WorkoutEndingScreen(navController)
        }
        composable(Screen.DAY_WORKOUT) {
            DayWorkoutScreen(navController)
        }
        composable(Screen.EXPRESS_WORKOUT_SELECTION) {
            ExpressWorkoutSelectionScreen(navController)
        }
        composable(Screen.EXPRESS_TIME_SETTING) {
            ExpressTimeSettingScreen(navController)
        }
        composable(Screen.EXPRESS_PREFERENCE) {
            ExpressPreferenceScreen(navController)
        }
        composable(Screen.FULFILLED_EXERCISE) {
            FulfilledExerciseScreen(navController)
        }
        composable(Screen.FULFILLED_ACTIVITY) {
            FulfilledActivityScreen(navController)
        }
        composable(Screen.FULFILLED_CARDIO_INTENSITY) {
            FulfilledCardioIntensityScreen(navController)
        }
        composable(Screen.FULFILLED_STRENGTH_INTENSITY) {
            FulfilledStrengthIntensityScreen(navController)
        }
        composable(Screen.REGISTERED_ACTIVITY) {
            RegisteredActivityScreen(navController)
        }
    }

}

