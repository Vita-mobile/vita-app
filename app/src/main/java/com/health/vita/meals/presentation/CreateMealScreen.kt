package com.health.vita.meals.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.components.meals.BorderLabelText
import com.health.vita.ui.theme.LightGray


@Composable
fun CreateMealScreen(
    navController: NavController = rememberNavController(),
    createMealViewModel: CreateMealViewModel = viewModel()
) {
    val uiState by createMealViewModel.uiState.observeAsState(UiState.Idle)
    var searchQuery by remember { mutableStateOf("") }
    val ingredientsState by createMealViewModel.ingredientsState.observeAsState(emptyList())
    val addedIngredientsState by createMealViewModel.addedIngredientsState.observeAsState(emptyList())
    val mealCreationSuccess by createMealViewModel.mealCreationSuccess.observeAsState(false)

    var showMealCreationDialog by remember { mutableStateOf(false) }

    if (mealCreationSuccess && !showMealCreationDialog) {
        showMealCreationDialog = true
    }

    if (showMealCreationDialog) {
        AlertDialog(
            onDismissRequest = { showMealCreationDialog = false },
            title = { Text("Comida creada") },
            text = { Text("La comida se ha creado exitosamente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showMealCreationDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Continuar")
                }
            }
        )
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GeneralTopBar(
                    text = "Crear",
                    onClick = { navController.navigateUp() },
                    hasStep = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = createMealViewModel.mealName.observeAsState("").value,
                    onValueChange = { createMealViewModel.setMealName(it) },
                    label = { Text("Nombre de la comida") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            LightGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar ingrediente") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            LightGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
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
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val filteredIngredients = ingredientsState.filter {
                                it?.name!!.contains(searchQuery, ignoreCase = true)
                            }

                            if (filteredIngredients.isEmpty()) {
                                item {
                                    BorderLabelText(
                                        text = "No se encontraron ingredientes",
                                        modifier = Modifier,
                                        border = false,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                    )
                                }
                            } else {
                                items(filteredIngredients.chunked(2)) { chunk ->

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                    ) {
                                        chunk.forEach() { ingredient ->
                                            val isAdded =
                                                addedIngredientsState.any { it.first == ingredient }

                                            BorderLabelText(
                                                text = ingredient?.name
                                                    ?: "Ingrediente no disponible",
                                                modifier = Modifier,
                                                border = false,
                                                color = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.8f
                                                ),
                                                icon = Icons.Outlined.Add,
                                                onIconClick = {
                                                    if (!isAdded) {
                                                        ingredient?.let {
                                                            createMealViewModel.addIngredientToMeal(
                                                                it,
                                                                100
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))

                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))

                                }
                            }
                        }

                        if (addedIngredientsState.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No se han añadido ingredientes", textAlign = TextAlign.Center)
                            }
                        } else {


                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentPadding = PaddingValues(vertical = 8.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                items(items = addedIngredientsState.toList()) { (ingredient, grams) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        BorderLabelText(
                                            text = ingredient.name,
                                            border = false,
                                            color = MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.8f
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        TextField(
                                            value = grams.toString(),
                                            onValueChange = { newGrams ->
                                                val newGramsInt = newGrams.toIntOrNull() ?: 0
                                                createMealViewModel.updateIngredientGram(
                                                    ingredient,
                                                    newGramsInt
                                                )
                                            },
                                            label = { Text("Gramos") },
                                            keyboardOptions = KeyboardOptions.Default.copy(
                                                keyboardType = KeyboardType.Number
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .border(
                                                    1.dp,
                                                    LightGray,
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                                .clip(RoundedCornerShape(20.dp)),
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent
                                            )
                                        )
                                        IconButton(
                                            onClick = {
                                                createMealViewModel.removeIngredientFromMeal(
                                                    ingredient
                                                )
                                            },
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Close,
                                                contentDescription = "Close"
                                            )
                                        }
                                    }
                                }
                            }


                        }

                        PrimaryIconButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            text = "Crear Comida",
                            onClick = { createMealViewModel.createMeal() },
                            enabled = (addedIngredientsState.isNotEmpty() && createMealViewModel.mealName.observeAsState(
                                ""
                            ).value != "")
                        )
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