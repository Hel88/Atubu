package com.example.atubu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.atubu.ui.PlantScreen
import com.example.atubu.ui.PlantViewModel
import com.example.atubu.ui.StartSettingScreen
import com.example.atubu.ui.Friend
import com.example.atubu.ui.ComingSoonScreen
import com.example.atubu.ui.ShowGarden


/**
 * enum values that represent the screens in the app
 */
enum class AtubuScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Garden(title = R.string.garden),
    Setting(title = R.string.setting),
    Friend(title=R.string.friend),
    Success(title = R.string.success)
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
    drawerState: DrawerState,

    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
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
                } else {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }

            }
        )
}

@Composable
fun NavDrawer(){


}


@Composable
fun AtubuApp(

    navController : NavHostController = rememberNavController()
){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AtubuScreen.valueOf(
        backStackEntry?.destination?.route ?: AtubuScreen.Start.name
    )
    val image = painterResource(id = R.drawable.friend)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            DismissibleDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))

                    NavigationDrawerItem(
                        label = { Text("Jardin") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.DateRange, contentDescription = null) },
                        onClick = { navController.navigate(AtubuScreen.Garden.name)}
                    )
                    NavigationDrawerItem(
                        label = { Text("Amis") },
                        selected = false,
                        icon = { Icon(painter = image, contentDescription = "friend", modifier = Modifier.size(24.dp)) },
                        onClick = { navController.navigate(AtubuScreen.Friend.name)}
                    )
                    NavigationDrawerItem(
                        label = { Text("Succès") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Star, contentDescription = null) },
                        onClick = { navController.navigate(AtubuScreen.Success.name)}
                    )
                    NavigationDrawerItem(
                        label = { Text("Paramètres") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        onClick = {navController.navigate(AtubuScreen.Setting.name)}
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {

        Scaffold(
            topBar = {
                AtubuAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    drawerState = drawerState,

                )
            }
        ) { innerPadding ->


            NavHost(
                navController = navController,
                startDestination = AtubuScreen.Start.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = AtubuScreen.Garden.name) {
                    ShowGarden()
                }
                composable(route = AtubuScreen.Start.name) {
                    PlantScreen()
                }
                composable(route = AtubuScreen.Setting.name) {
                    StartSettingScreen(
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                composable(route = AtubuScreen.Friend.name) {
                    Friend()
                }
                composable(route = AtubuScreen.Success.name) {
                    ComingSoonScreen()
                }


            }
        }
    }
}