package com.health.vita.core.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.health.vita.auth.presentation.ResetPasswordConfirmationScreen
import com.health.vita.auth.presentation.ResetPasswordScreen
import com.health.vita.auth.presentation.login.LoginScreen
import com.health.vita.main.presentation.AccountSettingsScreen
import com.health.vita.main.presentation.HomeScreen
import com.health.vita.main.presentation.LoadSimulationScreen
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
import com.health.vita.profile.presentation.EditWeightScreen
import com.health.vita.profile.presentation.NotificationsScreen
import com.health.vita.profile.presentation.ProfileEditionScreen
import com.health.vita.profile.presentation.ProfileScreen
import com.health.vita.register.presentation.AgeSelectionScreen
import com.health.vita.register.presentation.FitnessGoalSelectionScreen
import com.health.vita.register.presentation.FitnessLevelSelectionScreen
import com.health.vita.register.presentation.HeightSelectionScreen
import com.health.vita.register.presentation.SelectAvatarScreen
import com.health.vita.register.presentation.SexSelectionScreen
import com.health.vita.register.presentation.SignUpScreen
import com.health.vita.register.presentation.WeightSelectionScreen
import com.health.vita.register.presentation.viewmodel.SignupViewModel
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    val signupViewModel: SignupViewModel = viewModel()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.LOAD_SIMULATION,
        // Transición de entrada cuando se navega a una nueva pantalla
        // Aquí se define un deslizamiento horizontal desde la derecha y un desvanecimiento.
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
        },
        // Transición de salida cuando se navega desde una pantalla actual a otra
        // Aquí se define un deslizamiento horizontal hacia la izquierda y un desvanecimiento.
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
        },
        // Transición de entrada cuando se navega hacia atrás (pop) a una pantalla anterior
        // Se define un deslizamiento horizontal desde la izquierda y un desvanecimiento.
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
        },
        // Transición de salida cuando se retrocede (pop) a la pantalla anterior
        // Aquí se define un deslizamiento horizontal hacia la derecha y un desvanecimiento.
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
        }
    ) {
        // Aquí continúan las definiciones de las pantallas y su navegación
        // Main
        composable(Screen.LOAD_SIMULATION) {
            LoadSimulationScreen(navController)
        }
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
        composable(Screen.RESET_PASSWORD) {
            ResetPasswordScreen(navController)
        }
        composable(Screen.RESET_PASSWORD_CONFIRMATION) {
            ResetPasswordConfirmationScreen(navController)
        }

        // Signup
        composable(Screen.SIGN_UP) {
            SignUpScreen(navController, signupViewModel)
        }
        composable(Screen.SELECT_AVATAR) {
            SelectAvatarScreen(navController)
        }
        composable(Screen.AGE_SELECTION) {
            AgeSelectionScreen(navController, signupViewModel)
        }
        composable(Screen.WEIGHT_SELECTION) {
            WeightSelectionScreen(
                navController = navController,
                signupViewModel = signupViewModel
            )
        }
        composable(Screen.HEIGHT_SELECTION) {
            HeightSelectionScreen(
                navController = navController,
                signupViewModel = signupViewModel
            )
        }
        composable(Screen.FITNESS_LEVEL_SELECTION) {
            FitnessLevelSelectionScreen(navController, signupViewModel)
        }
        composable(Screen.SEX_SELECTION) {
            SexSelectionScreen(navController, signupViewModel)
        }
        composable(Screen.FITNESS_GOAL_SELECTION) {
            FitnessGoalSelectionScreen(navController, signupViewModel)
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
        composable(Screen.EDIT_WEIGHT_SELECTION) {
            EditWeightScreen(navController)
        }
        composable(Screen.EDIT_HEIGHT_SELECTION) {
            EditWeightScreen(navController)
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
