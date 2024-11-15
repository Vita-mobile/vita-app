package com.health.vita.register.presentation

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.health.vita.R
import com.health.vita.core.navigation.Screen
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.register.presentation.viewmodel.SignupViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton


@Composable
fun SelectAvatarScreen(navController: NavController = rememberNavController(),
                       signupViewModel: SignupViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        signupViewModel.loadDefaultImages()
    }

    val defaultAvatars by signupViewModel.defaultImages.observeAsState(emptyList())

    val selectedAvatar by signupViewModel.profileImage.observeAsState()

    val listState = rememberLazyListState()
    val uiState by signupViewModel.uiState.observeAsState(UiState.Idle)

    var isLoading by remember { mutableStateOf(true) }

    var selectedUri:Uri? by remember { mutableStateOf(null) }

    var infoSignup by remember {

        mutableStateOf("")
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri

        if (uri != null) {
            signupViewModel.selectUploadedImage(uri)
        }

    }

    LaunchedEffect(defaultAvatars) {
        if (defaultAvatars.isNotEmpty()) {
            isLoading = false
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.weight(0.02f))

                GeneralTopBar(
                    text = "",
                    hasStep = false,
                    lightMode = true,
                    hasIcon = false,
                    onClick = { navController.navigateUp() },
                    onClickIcon = {}
                )

                Box(modifier = Modifier.weight(0.4f))


                Text(
                    text = "Selecciona tu foto",
                    style = MaterialTheme.typography.titleLarge
                )

                Box(modifier = Modifier.weight(0.6f))


                LazyRow(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    itemsIndexed(defaultAvatars) {_,imageUrl ->

                        val size by animateDpAsState(targetValue = if (selectedAvatar == imageUrl) 120.dp else 100.dp,
                            label = ""
                        )

                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(size)
                                .clip(CircleShape)
                                .clickable {

                                    if (selectedUri != null) {
                                        selectedUri = null
                                    }
                                    signupViewModel.selectDefaultImage(imageUrl)
                                }
                                .border(
                                    BorderStroke(
                                        4.dp,
                                        if (selectedAvatar == imageUrl) MaterialTheme.colorScheme.primary else Color.Transparent
                                    ),
                                    CircleShape
                                )
                        ){

                            if(isLoading){

                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            else{

                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = "Avatar",
                                    contentScale = ContentScale.Crop
                                )

                            }
                        }


                        Spacer(modifier = Modifier.width(16.dp))
                    }




                }

                Box(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "O sube un archivo local",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .drawBehind {
                                //Rounded corners rectangle
                                drawRoundRect(
                                    color = Color.Black,
                                    style = Stroke(
                                        width = 0.6.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(
                                                25f,
                                                25f
                                            ), 0f
                                        ) //generates the dashed line border of the rectangle
                                    ),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(60.dp.toPx()) // Sets the radius of rounded corners
                                )
                            }
                            .padding(vertical = 16.dp),
                    ) {

                        Box(
                            modifier = Modifier.fillMaxSize().clickable {

                                if (selectedAvatar != null) {
                                    signupViewModel.selectDefaultImage(null)
                                }

                                launcher.launch("image/*")
                            },
                            contentAlignment = Alignment.BottomCenter

                        ) {

                            if(selectedUri!=null) {

                                Image(
                                    painter = rememberAsyncImagePainter(selectedUri),
                                    contentDescription = "selected image",
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)),
                                    contentScale = ContentScale.Crop
                                )

                            } else {

                                Box(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFFA8F5D7)),

                                    ) {


                                }

                                Icon(
                                    painter = painterResource(R.drawable.round_arrow_upward_24),
                                    contentDescription = "arrow",
                                    tint = MaterialTheme.colorScheme.onTertiary,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .offset(y = (-30).dp)
                                )
                            }

                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Formato: jpg, png",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                        ),
                    )

                }

                Box(modifier = Modifier.weight(0.8f))

                PrimaryIconButton(
                    text = "Comenzar",
                    color = if (selectedAvatar != null) Color.Black else Color.Gray,
                    onClick = {

                        signupViewModel.registerOperation()

                    },
                    enabled = selectedAvatar != null,
                )

                Box(modifier = Modifier.weight(0.2f))

                Text(
                    text = infoSignup,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )

                when (uiState) {

                    is UiState.Idle -> {

                        infoSignup = ""

                    }

                    is UiState.Loading -> {


                        CircularProgressIndicator()
                    }

                    is UiState.Success -> {

                        infoSignup = "Registro exitoso"

                        if (selectedAvatar != null) {
                            signupViewModel.updateProfileImage()

                            navController.navigate(Screen.HOME)
                        }

                    }

                    is UiState.Error -> {

                        val error = (uiState as UiState.Error).error
                        Log.e( "SING-UP", error.message)
                        infoSignup = "Error al realizar el registro."

                    }

                }

            }

        }

    )

}

