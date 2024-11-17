package com.health.vita.meals.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.presentation.viewModels.CreateMealViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import com.health.vita.core.utils.states_management.UiState


@Composable
fun CreateMealScreen(
    navController: NavController = rememberNavController(),
    createMealViewModel: CreateMealViewModel = viewModel()
) {
    val uiState by createMealViewModel.uiState.observeAsState(UiState.Idle)
    var searchQuery by remember { mutableStateOf("") }
    val ingredientsState by createMealViewModel.ingredientsState.observeAsState(emptyList())
    val addedIngredientsState by createMealViewModel.addedIngredientsState.observeAsState(emptyList())

    val filteredIngredients = ingredientsState.filter { it?.name!!.contains(searchQuery, ignoreCase = true) }

    fun addIngredient(ingredient: Ingredient, grams: Int) {
        createMealViewModel.addIngredientToMeal(ingredient, grams)
    }

    fun removeIngredient(ingredient: Ingredient) {
        createMealViewModel.removeIngredientFromMeal(ingredient)
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                GeneralTopBar(
                    text = "Crear",
                    onClick = { navController.popBackStack() },
                    hasStep = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar ingrediente") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (uiState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            if (filteredIngredients.isEmpty()) {
                                item {
                                    Text("No se encontraron ingredientes", modifier = Modifier.padding(16.dp))
                                }
                            } else {
                                items(filteredIngredients.take(3)) { ingredient ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(8.dp)
                                            .clickable {
                                                ingredient?.let { addIngredient(it, 100) }
                                            }
                                    ) {
                                        Text(
                                            text = ingredient?.name ?: "Ingrediente no disponible"
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column {
                            addedIngredientsState.forEach { (ingredient, grams) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(ingredient.name, modifier = Modifier.weight(1f))
                                    TextField(
                                        value = grams.toString(),
                                        onValueChange = { newGrams ->
                                            val newGramsInt = newGrams.toIntOrNull() ?: 0
                                            createMealViewModel.updateIngredientGram(ingredient, newGramsInt)
                                        },
                                        label = { Text("Gramos") },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        modifier = Modifier.weight(1f),
                                    )
                                    Button(
                                        onClick = { removeIngredient(ingredient) },
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text("-")
                                    }
                                }
                            }

                            if (addedIngredientsState.isEmpty()) {
                                Text("No se han añadido ingredientes")
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { createMealViewModel.createMeal() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Crear Comida")
                            }
                        }
                    }
                    is UiState.Error -> {
                        Text("Error al cargar los ingredientes", modifier = Modifier.padding(16.dp))
                    }
                    else -> {

                    }
                }
            }
        }
    )
}
