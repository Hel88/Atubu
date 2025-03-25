package com.example.atubu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.atubu.ui.PlantScreen
import com.example.atubu.ui.PlantViewModel
import com.example.atubu.ui.StartSettingScreen


/**
 * enum values that represent the screens in the app
 */
enum class AtubuScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Setting(title = R.string.setting)

}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtubuAppBar(
    currentScreen: AtubuScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }
        }
    )
}

@Composable
fun AtubuApp(
    viewModel: PlantViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AtubuScreen.valueOf(
        backStackEntry?.destination?.route ?: AtubuScreen.Start.name
    )
    Scaffold(
        topBar={
            AtubuAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) {
        innerPadding ->


        NavHost(
            navController = navController,
            startDestination = AtubuScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = AtubuScreen.Start.name) {
                PlantScreen(
                    onNextButtonClicked = {
                        navController.navigate(AtubuScreen.Setting.name)
                    },
                )
            }
            composable(route = AtubuScreen.Setting.name) {
                StartSettingScreen(
                    modifier = Modifier.fillMaxHeight()
                )
            }

        }
    }

}