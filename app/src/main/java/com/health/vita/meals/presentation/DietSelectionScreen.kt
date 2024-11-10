package com.health.vita.meals.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.presentation.viewModels.IngredientViewModel
import com.health.vita.meals.presentation.viewModels.NutritionalPlanViewModel
import com.health.vita.ui.components.general.CustomPopup
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.components.meals.BorderLabelText
import com.health.vita.ui.components.meals.DualIconCurvedButtons
import com.health.vita.ui.theme.MintGreen

@Preview
@Composable
fun DietSelectionScreen(
    navController: NavController = rememberNavController(),
    ingredientViewModel: IngredientViewModel = viewModel(),
    nutritionalPlanViewModel: NutritionalPlanViewModel = viewModel()
) {
    val ingredientState by ingredientViewModel.ingredientsState.observeAsState(listOf())
    val preferences by nutritionalPlanViewModel.preferences.observeAsState(listOf())
    val restrictions by nutritionalPlanViewModel.restrictions.observeAsState(listOf())
    val meals by nutritionalPlanViewModel.meals.observeAsState(3)
    val searchQuery by ingredientViewModel.searchQuery.observeAsState("")
    var isSettingPreferences = false
    var ingredientPopUp by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        ingredientViewModel.getIngredients()
    }

    Scaffold(modifier = Modifier.fillMaxSize(), content = { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GeneralTopBar(
                text = "Nutrición",
                hasStep = false,
                onClick = { navController.navigateUp() })

            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    bottom = 16.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
                text = "Te ayudaremos a crear un plan de comidas solo para ti.",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(0.4f))
            BorderLabelText(
                text = "Define cuántas comidas deseas realizar",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )
            Spacer(modifier = Modifier.weight(0.4f))
            Row(
                modifier = Modifier.fillMaxWidth(.6f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BorderLabelText(
                    text = "$meals",
                    border = false,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(32.dp))
                DualIconCurvedButtons(
                    modifier = Modifier.height(48.dp),
                    bgColor = Color.Transparent,
                    border = true,
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    leftIconOnClick = {nutritionalPlanViewModel.setMeals(meals-1)},
                    rightIconOnClick = {nutritionalPlanViewModel.setMeals(meals+1)},
                )
            }
            Spacer(modifier = Modifier.weight(0.4f))
            BorderLabelText(
                text = "Cuéntanos sobre los alimentos que quieres evitar",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                icon = Icons.Outlined.Add,
                onIconClick = {
                    ingredientPopUp = !ingredientPopUp
                    isSettingPreferences = false
                }
            )
            Spacer(modifier = Modifier.weight(0.4f))
            if (restrictions.isEmpty()) {
                Column(
                    modifier = Modifier.weight(5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BorderLabelText(
                        text = "Agrega ingredientes para conocerte más, ve",
                        border = false,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(5f), verticalArrangement = Arrangement.Center) {
                    items(restrictions.chunked(2)) { itemGroup ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            itemGroup.forEach { item ->
                                BorderLabelText(
                                    text = item.name,
                                    border = false,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.4f))
            BorderLabelText(
                text = "Cuéntanos sobre los alimentos a los que tienes fácil acceso",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                icon = Icons.Outlined.Add,
                onIconClick = {
                    ingredientPopUp = !ingredientPopUp
                    isSettingPreferences = true
                }
            )
            Spacer(modifier = Modifier.weight(0.4f))
            if (preferences.isEmpty()) {
                Column(
                    modifier = Modifier.weight(5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BorderLabelText(
                        text = "Agrega ingredientes para conocerte más, ve",
                        border = false,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(5f), verticalArrangement = Arrangement.Center) {
                    items(preferences.chunked(2)) { itemGroup ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            itemGroup.forEach { item ->
                                BorderLabelText(
                                    text = item.name,
                                    border = false,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.4f))
            PrimaryIconButton(text = "Continuar", color = MintGreen, onClick = {nutritionalPlanViewModel.createNutritionalPlan()})
            CustomPopup(
                showDialog = ingredientPopUp,
                onDismiss = { /*TODO*/ },
                onCancel = { ingredientPopUp = !ingredientPopUp },
                onConfirm = { ingredientPopUp = !ingredientPopUp },
                title = "Selecciona los ingredientes",
                height = 0.6f,
                content = {
                    Column(modifier = Modifier) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { query -> ingredientViewModel.setSearchQuery(query) },
                            label = { Text("Buscar ingrediente") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        val filteredIngredients = ingredientState.filter {
                            it?.name?.contains(
                                searchQuery,
                                ignoreCase = true
                            ) == true && it !in preferences && it !in restrictions
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(modifier = Modifier) {
                            items(filteredIngredients.chunked(2)) { itemGroup ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    itemGroup.forEach { item ->
                                        item?.let {
                                            BorderLabelText(
                                                text = it.name,
                                                border = false,
                                                color = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.8f
                                                ),
                                                icon = Icons.Outlined.Add,
                                                onIconClick = {
                                                    if (isSettingPreferences) {
                                                        nutritionalPlanViewModel.addPreference(it)
                                                    } else {
                                                        nutritionalPlanViewModel.addRestriction(it)
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                })
        }
    })
}

